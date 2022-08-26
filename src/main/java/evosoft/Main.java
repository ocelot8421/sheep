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

        Long locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
        Long locationCharger = locationRoboSheep;
        CoordinateDataStore coordinatesMowedField;

        int numberOfMoves = 100;
        for (int i = 0; i < numberOfMoves; i++) {
            if (!battery.needCharge()) {


                CoordinateDataStore neighbourLawnFields =
                        searchingUnit.findNeighbourLawnFields(locationRoboSheep, coordinatesLawn);
                locationRoboSheep = neighbourLawnFields.getCoordinates().get(0);
//                System.out.println(roboSheepCoordinatesStore);
                System.out.println(locationRoboSheep);
                System.out.println(neighbourLawnFields);

                mowerUnit.mow(roboSheepCoordinatesStore, neighbourLawnFields, coordinatesLawn);
                printMapFromCoordinatesStore(
                        getGardenWidth(), getGardenLength(),
                        locationRoboSheep, locationCharger,
                        roboSheepCoordinatesStore, coordinatesLawn);
                keepDistanceBetweenScreenshots(3);
                battery.saveChargeLevelAfterMovement();
            } else {
                System.out.println("RoboSheep need to go back to charge.");
                break; //TODO sheep need to go back to the charge
            }
        }
    }

}
