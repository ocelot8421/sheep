package evosoft;

import static evosoft.GardenMapImporter.getGardenLength;
import static evosoft.GardenMapImporter.getGardenWidth;
import static evosoft.ScreenPrinter.*;

public class MowerUnit {
    public void mow(
            RoboSheepCoordinatesStore roboSheepCoordinatesStore,
            Long nextLocationRoboSheep,
            CoordinateDataStore coordinatesLawn) {
        roboSheepCoordinatesStore.addConvertedCoordinates(nextLocationRoboSheep);
        coordinatesLawn.removeConvertedCoordinate(nextLocationRoboSheep);
    }


}
