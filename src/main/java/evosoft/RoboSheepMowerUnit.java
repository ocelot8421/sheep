package evosoft;

import java.util.List;

public class RoboSheepMowerUnit {

    public void mow(CoordinateDataStore neighbourLawnFields, CoordinateDataStore coordinatesLawn) {
        coordinatesLawn.removeCoordinate(neighbourLawnFields.getCoordinates().get(0));
        String marker = "?";

    }
}
