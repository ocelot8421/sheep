package evosoft;

import java.util.*;

public class SearchingUnit extends CoordinateDataStore {

    public SearchingUnit() {
    }

    public SearchingUnit(String name) {
        super(name);
    }

    public CoordinateDataStore findAndSortNeighbourTypeFields(
            String name, Long previousLocationRoboSheep, Long locationRoboSheep, CoordinateDataStore coordinatesTypeFieldsDB
    ) {
        List<Long> distanceOfNeighbours = Arrays.asList( // clockwise
                -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L);
        long stepVector = locationRoboSheep - previousLocationRoboSheep;
        distanceOfNeighbours = neighbourFieldsSorter(stepVector, distanceOfNeighbours);
        CoordinateDataStore neighbourFieldsSelected = new CoordinateDataStore(name);
        long coordinateNeighbourField;
        for (Long distance : distanceOfNeighbours) {
            coordinateNeighbourField = locationRoboSheep + distance;
            if (coordinatesTypeFieldsDB.getCoordinates().contains(coordinateNeighbourField)) {
                neighbourFieldsSelected.addConvertedCoordinates(coordinateNeighbourField);
            }
        }
        return neighbourFieldsSelected;
    }


    public Long findNearestField(Long spot, CoordinateDataStore coordinatesOfArea) {
        double minDistance = Math.sqrt(2) * 1000000;
        long coordinateXSheep = spot % 1000000;
        long coordinateYSheep = (spot - coordinateXSheep) / 1000000;
        long coordinateX;
        long coordinateY;
        Long nearestField = null;
        for (Long lawnCoordinate : coordinatesOfArea.getCoordinates()) {
            coordinateX = lawnCoordinate % 1000000;
            coordinateY = (lawnCoordinate - coordinateX) / 1000000;
            long distanceX = coordinateXSheep - coordinateX;
            long distanceY = coordinateYSheep - coordinateY;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            if (distance < minDistance) {
                minDistance = distance;
                nearestField = lawnCoordinate;
            }
        }
        return nearestField;
    }

    public CoordinateDataStore receivePerimeterSegment(
            CoordinateDataStore perimeterWay,
            long locationRoboSheep,
            long nearestPerimeterFieldOfNearestLawn) {
        int startAround = perimeterWay.getCoordinates().indexOf(locationRoboSheep);
        int stopAround = perimeterWay.getCoordinates().indexOf(nearestPerimeterFieldOfNearestLawn);
        CoordinateDataStore perimeterSegment = new CoordinateDataStore("Perimeter segment coordinate");
        Long perimeterCoordinate;
        for (int i = startAround; i < stopAround; i++) {
            perimeterCoordinate = perimeterWay.getCoordinates().get(i);
            perimeterSegment.addConvertedCoordinates(perimeterCoordinate);
        }
        return perimeterSegment;
    }

    public CoordinateDataStore findSection(
            CoordinateDataStore neighbourMowedFields, CoordinateDataStore perimeterWay) {
        CoordinateDataStore section = new CoordinateDataStore("Section area");
        for (Long neighbourField : neighbourMowedFields.getCoordinates()) {
            if (perimeterWay.getCoordinates().contains(neighbourField)) {
                section.addConvertedCoordinates(neighbourField);
            }
        }
        return section;
    }

//    public CoordinateDataStore findDetour(
//            long nearestPerimeterFieldToRoboSheep, long nearestPerimeterFieldTofNearestLawn, CoordinateDataStore perimeterWayCoorected) {
//        CoordinateDataStore detour = new CoordinateDataStore("Detour");
//        List<Long> helperDetour = new CopyOnWriteArrayList<>();
//        List<Long> helperPerimeterWayCorrected = new CopyOnWriteArrayList<>();
//        helperPerimeterWayCorrected.addAll(perimeterWayCoorected.getCoordinates());
//        int startIndex = helperPerimeterWayCorrected.indexOf(nearestPerimeterFieldToRoboSheep);
//        int endIndex = helperPerimeterWayCorrected.indexOf(nearestPerimeterFieldTofNearestLawn);
//        if (startIndex <= endIndex) {
//            helperDetour.addAll(perimeterWayCoorected.getCoordinates().subList(startIndex, endIndex + 1));
//        } else {
//            helperDetour.addAll(perimeterWayCoorected.getCoordinates().subList(endIndex, endIndex + 1));
//            helperDetour.sort(Collections.reverseOrder());
//        }
//        detour.setCoordinates(helperDetour);
//        return detour;
//    }

    // subList does not work
    public CoordinateDataStore receiveCorrectedPerimeterWay(
            CoordinateDataStore perimeterWay, Long locationCharge
    ) {
        CoordinateDataStore perimeterCorrected = new CoordinateDataStore("Corrected perimeter way");
        Long nearestFiled = findNearestField(locationCharge, perimeterWay);
        int endIndex = perimeterWay.getCoordinates().indexOf(nearestFiled);
        perimeterCorrected.setCoordinates(perimeterWay.getCoordinates().subList(0, endIndex));
        return perimeterCorrected;
    }

    private List<Long> neighbourFieldsSorter(long stepVector, List<Long> distanceOfNeighbours) {
        switch ((int) stepVector) { //TODO it should be nicer (part of one list are repeating)
            case 1:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L);
                break;
            case 1000001:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L);
                break;
            case 1000000:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L);
                break;
            case 999999:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L);
                break;
            case -1:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L);
                break;
            case -1000001:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L);
                break;
            case -1000000:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L);
                break;
            case -999999:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L);
                break;
        }
        return distanceOfNeighbours;
    }

    //subList() and sort() does not work
    public List<Long> findLineSegment(Long spotNearLineStart, Long sportNearLineEnd, List<Long> way) {
        List<Long> result = new ArrayList<>();
        int indexAroundSheep = 0;
        int indexAroundLawn = 0;
        for (int i = 0; i < way.size(); i++) {
            if (Objects.equals(spotNearLineStart, way.get(i))) {
                indexAroundSheep = i;
            }
            if (Objects.equals(sportNearLineEnd, way.get(i))) {
                indexAroundLawn = i;
            }
        }
        if (indexAroundSheep < indexAroundLawn) {
            for (int j = indexAroundSheep; j < Math.abs(indexAroundLawn - indexAroundSheep); j++) {
                result.add(way.get(j));
            }
        } else {
            for (int j = indexAroundLawn; j > Math.abs(indexAroundLawn - indexAroundSheep) * 2 ; j--) {
                result.add(way.get(j));
            }
        }
        if (result.size() == 0){
            for (int j = 0; j > way.size() ; j--) {
                result.add(way.get(j));
            }
        }

        return result;
    }
}
