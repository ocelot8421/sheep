package evosoft;

public class RoboSheepMowerUnit {
    public void mow(
            RoboSheepCoordinatesStore roboSheepCoordinatesStore,
            Long nextLocationRoboSheep,
            CoordinateDataStore coordinatesLawn) {
//        if (neighbourLawnFields.getCoordinates().size() > 0) {
//            Long nextStepCoordinate = neighbourLawnFields.getCoordinates().get(0);
        roboSheepCoordinatesStore.addConvertedCoordinates(nextLocationRoboSheep);
        coordinatesLawn.removeConvertedCoordinate(nextLocationRoboSheep);
//        }
    }
}
