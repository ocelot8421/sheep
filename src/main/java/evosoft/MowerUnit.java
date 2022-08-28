package evosoft;

public class MowerUnit {
    public void mow(
            RoboSheepCoordinatesStore roboSheepCoordinatesStore,
            Long nextLocationRoboSheep,
            CoordinateDataStore coordinatesLawn) {
        roboSheepCoordinatesStore.addConvertedCoordinates(nextLocationRoboSheep);
        coordinatesLawn.removeConvertedCoordinate(nextLocationRoboSheep);
    }

}
