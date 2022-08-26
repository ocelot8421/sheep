package evosoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoboSheepSearchingUnit extends CoordinateDataStore {

    public CoordinateDataStore findNeighbourLawnFields(RoboSheepCoordinatesStore coordinatesStoreOfRoboSheep, CoordinateDataStore coordinatesLawn) {
        Long locationRoboSheep = coordinatesStoreOfRoboSheep.receiveLastLocation();
        List<Long> distanceOfNeighbours = Arrays.asList(
                -1000001L, -1000000L, -999999L,
                -1L, 1L,
                999999L, 1000000L, 1000001L);
//        List<Long> neighbourLawnFields = new ArrayList<>();
        CoordinateDataStore neighbourLawnFields = new CoordinateDataStore();
        neighbourLawnFields.setName("Coordinates of neighbour lawn fields");
        Long coordinateNeighbourField;
        for (Long distance : distanceOfNeighbours) {
            coordinateNeighbourField = locationRoboSheep + distance;
            if (coordinatesLawn.getCoordinates().contains(coordinateNeighbourField)) {
                neighbourLawnFields.addConvertedCoordinate(0, coordinateNeighbourField);
            }
        }
        System.out.println(locationRoboSheep);
        return neighbourLawnFields;
    }
}
