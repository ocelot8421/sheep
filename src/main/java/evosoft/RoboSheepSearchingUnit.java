package evosoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoboSheepSearchingUnit extends CoordinateDataStore {

    public CoordinateDataStore findAndSortNeighbourTypeFields(Long previousLocationRoboSheep, Long locationRoboSheep, CoordinateDataStore coordinatesTypeFieldsDB) {
        List<Long> distanceOfNeighbours = Arrays.asList( // clockwise
                -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L);
        long stepVector = locationRoboSheep - previousLocationRoboSheep;
        distanceOfNeighbours = neighbourFieldsSorter(stepVector, distanceOfNeighbours);
        CoordinateDataStore neighbourFieldsSelected = new CoordinateDataStore();
        long coordinateNeighbourField;
        for (Long distance : distanceOfNeighbours) {
            coordinateNeighbourField = locationRoboSheep + distance;
            if (coordinatesTypeFieldsDB.getCoordinates().contains(coordinateNeighbourField)) {
                neighbourFieldsSelected.addConvertedCoordinates(coordinateNeighbourField);
            }
        }
        return neighbourFieldsSelected;
    }

    private List<Long> neighbourFieldsSorter(long stepVector, List<Long> distanceOfNeighbours) {
        switch ((int) stepVector) { //TODO it should be nicer (part of one list are repeating)
            case 1:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L);
//                        -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L);
//                        1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L);
                break;
            case 1000001:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L);
//                        -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L);
//                         1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L);
                break;
            case 1000000:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L);
//                        -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L);
//                        1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L);
                break;
            case 999999:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L);
//                        -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L);
//                        999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L);
                break;
            case -1:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L);
//                        1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L);
//                        -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L);
                break;
            case -1000001:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L);
//                        1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L);
//                        -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L);
                break;
            case -1000000:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L);
//                        1000000L, 999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L);
//                        -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L);
                break;
            case -999999:
                distanceOfNeighbours = Arrays.asList( // clockwise
                        -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L, 999999L);
//                        999999L, -1L, -1000001L, -1000000L, -999999L, 1L, 1000001L, 1000000L);
//                        -999999L, 1L, 1000001L, 1000000L, 999999L, -1L, -1000001L, -1000000L);
                break;
        }
        return distanceOfNeighbours;
    }

    public Long findNearestFields(Long spot, CoordinateDataStore coordinatesOfArea) {
        double minDistance = Math.sqrt(2) * 1000000;
        long coordinateXSheep = spot % 1000000;
        long coordinateYSheep = (spot - coordinateXSheep) / 1000000;
        long coordinateX;
        long coordinateY;
        Long nearestField = null;
        for (Long lawnCoordinate : coordinatesOfArea.getCoordinates()) {
//            System.out.println("lawnCoordinate: " + lawnCoordinate);
            coordinateX = lawnCoordinate % 1000000;
            coordinateY = (lawnCoordinate - coordinateX) / 1000000;
//            System.out.println("coordinateY: " + coordinateY);
//            System.out.println("coordinateX: " + coordinateX);
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
}
