package evosoft;

public class RoboSheepMowerUnit {
    public CoordinateDataStore mow(
            RoboSheepCoordinatesStore roboSheepCoordinatesStore,
            CoordinateDataStore neighbourLawnFields,
            CoordinateDataStore coordinatesLawn) {
        CoordinateDataStore coordinatesMowedField = new CoordinateDataStore();
        coordinatesLawn.setName("Coordinates of mowed fields");
        if (neighbourLawnFields.getCoordinates().size() > 1) {
            Long nextStepCoordinate = neighbourLawnFields.getCoordinates().get(0);
            roboSheepCoordinatesStore.addConvertedCoordinates(nextStepCoordinate);
            coordinatesLawn.removeConvertedCoordinate(nextStepCoordinate);
            coordinatesMowedField.addConvertedCoordinates(nextStepCoordinate); //-----
        }
        return coordinatesMowedField;
    }
}
