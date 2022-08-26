package evosoft;

import java.util.List;

import static evosoft.GardenMapImporter.importGardenMap;
import static evosoft.ScreenPrinter.keepDistanceBetweenScreenshots;

public class Main {
    public static void main(String[] args) {
        RoboSheepBattery battery = new RoboSheepBattery();
        RoboSheepCoordinatesStore roboSheepCoordinatesStore = new RoboSheepCoordinatesStore();
        roboSheepCoordinatesStore.setName("Coordinates of RoboSheep");
        RoboSheepSearchingUnit searchingUnit = new RoboSheepSearchingUnit();
        RoboSheepMowerUnit mowerUnit = new RoboSheepMowerUnit();

        CoordinateDataStore coordinatesLawn = importGardenMap(roboSheepCoordinatesStore);

        int numberOfMoves = 5;
        for (int i = 0; i < numberOfMoves; i++) {
            if (!battery.needCharge()) {
                System.out.println(roboSheepCoordinatesStore);

                CoordinateDataStore neighbourLawnFields = searchingUnit.findNeighbourLawnFields(roboSheepCoordinatesStore, coordinatesLawn);
                System.out.println(neighbourLawnFields);
                mowerUnit.mow(neighbourLawnFields, coordinatesLawn);
                battery.saveChargeLevelAfterMovement();
                keepDistanceBetweenScreenshots(3);
            } else {
                System.out.println("RoboSheep need to go back to charge.");
                break; //TODO sheep need to go back to the charge
            }
        }
    }

}
