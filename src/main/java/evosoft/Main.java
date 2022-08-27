package evosoft;

import java.util.List;

import static evosoft.GardenMapImporter.*;
import static evosoft.ScreenPrinter.*;

public class Main {
    public static void main(String[] args) {
        RoboSheepBattery battery = new RoboSheepBattery();
        RoboSheepCoordinatesStore roboSheepCoordinatesStore = new RoboSheepCoordinatesStore();
        roboSheepCoordinatesStore.setName("Coordinates of RoboSheep");
        RoboSheepSearchingUnit searchingUnit = new RoboSheepSearchingUnit();
        RoboSheepMowerUnit mowerUnit = new RoboSheepMowerUnit();

        CoordinateDataStore coordinatesLawn = importGardenMap(roboSheepCoordinatesStore);
        keepDistanceBetweenScreenshots(3);

        Long nextLocationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
        Long locationRoboSheep;
        Long locationCharger = nextLocationRoboSheep;

        int numberOfMoves = 1000000;
//        int numberOfMoves = 66;
        for (int i = 0; i < numberOfMoves; i++) {
            if (!battery.needCharge()) {
                System.out.println("Steps: " + i);
                CoordinateDataStore neighbourLawnFields =
                        searchingUnit.findNeighbourLawnFields(nextLocationRoboSheep, coordinatesLawn);
                nextLocationRoboSheep = neighbourLawnFields.getCoordinates().get(0);
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
//                System.out.println(roboSheepCoordinatesStore);
                System.out.println("RoboSheep last 5 step: " + roboSheepCoordinatesStore.receiveLast5Location());
                System.out.println("RoboSheep location: " + locationRoboSheep);

                System.out.println(neighbourLawnFields);
                System.out.println("RoboSheep next location: " + nextLocationRoboSheep);

                printMapFromCoordinatesStore(
                        getGardenWidth(), getGardenLength(),
                        locationRoboSheep, locationCharger,
                        roboSheepCoordinatesStore, coordinatesLawn);
                mowerUnit.mow(roboSheepCoordinatesStore, neighbourLawnFields, coordinatesLawn);
                battery.saveChargeLevelAfterMovement();
                keepDistanceBetweenScreenshots(3);
            } else {
                System.out.println("RoboSheep need to go back to charge.");
                break; //TODO sheep need to go back to the charge
            }
        }
    }

}
