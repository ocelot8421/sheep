package evosoft;

import java.util.Arrays;
import java.util.List;

public class RoboSheepSearchingUnit extends CoordinateDataStore {

    public CoordinateDataStore findNeighbourLawnFields(Long locationRoboSheep, CoordinateDataStore coordinatesLawn) {
        List<Long> distanceOfNeighbours = Arrays.asList( // clockwise
                -1000001L, -1000000L, -999999L,
                1L,
                 1000001L, 1000000L, 999999L,
                -1L);
        CoordinateDataStore neighbourLawnFields = new CoordinateDataStore();
        neighbourLawnFields.setName("Coordinates of neighbour lawn fields");
        long coordinateNeighbourField;
        for (Long distance : distanceOfNeighbours) {
            coordinateNeighbourField = locationRoboSheep + distance;
            if (coordinatesLawn.getCoordinates().contains(coordinateNeighbourField)) {
                neighbourLawnFields.addConvertedCoordinates(coordinateNeighbourField);
            }
        }
        return neighbourLawnFields;
    }
}
