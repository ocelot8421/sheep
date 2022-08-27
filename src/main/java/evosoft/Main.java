package evosoft;

import java.util.Objects;
import java.util.Random;

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

        Long locationCharger = roboSheepCoordinatesStore.receiveLastLocation();
        Long locationRoboSheep;
        Long nextLocationRoboSheep;
        Long previousLocationRoboSheep;

        int numberOfMoves = 1000000;
        for (int i = 0; i < numberOfMoves; i++) {
            if (!battery.needCharge()) {

                System.out.println("Steps: " + i);
                System.out.println("RoboSheep last 5 step: " + roboSheepCoordinatesStore.receiveLastTenLocation());

                previousLocationRoboSheep = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                System.out.println("RoboSheep previous location: " + previousLocationRoboSheep);
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                System.out.println("RoboSheep location: " + locationRoboSheep);
                CoordinateDataStore neighbourLawnFields =
                        searchingUnit.findAndSortNeighbourTypeFields(
                                previousLocationRoboSheep,
                                locationRoboSheep,
                                coordinatesLawn);
                neighbourLawnFields.setName("Neighbour  lawn fields");
                System.out.println(neighbourLawnFields);
                CoordinateDataStore neighbourMowedFields =
                        searchingUnit.findAndSortNeighbourTypeFields(
                                previousLocationRoboSheep,
                                locationRoboSheep,
                                roboSheepCoordinatesStore);
                neighbourMowedFields.setName("Neighbour mowed fields");

                if (neighbourLawnFields.getCoordinates().size() > 0) {
                    nextLocationRoboSheep = neighbourLawnFields.getCoordinates().get(0);
                } else {
                    long lastLocation7th = roboSheepCoordinatesStore.receiveLastTenLocation().get(6);
                    long lastLocation8th = roboSheepCoordinatesStore.receiveLastTenLocation().get(7);
                    long lastLocation9th = roboSheepCoordinatesStore.receiveLastTenLocation().get(8);
                    long lastLocation10th = roboSheepCoordinatesStore.receiveLastTenLocation().get(9);
                    Long nearestLawnField;
                    if (!coordinatesLawn.getCoordinates().isEmpty()) {
                        nearestLawnField = searchingUnit.findNearestFields(locationRoboSheep, coordinatesLawn);
                    } else {
                        nearestLawnField = locationCharger;
                    }
                    if (lastLocation8th == lastLocation10th && lastLocation7th == lastLocation9th) {
                        nearestLawnField -= 999999;
                    }
                    Long mowedFieldToNearestLawnFiled = searchingUnit
                            .findNearestFields(nearestLawnField, neighbourMowedFields);
                    nextLocationRoboSheep = mowedFieldToNearestLawnFiled;
                    System.out.println("Nearest lawn field: " + nearestLawnField);
                    System.out.println(
                            "Nearest mowed neighbour field to nearest lawn field: " +
                                    mowedFieldToNearestLawnFiled);
                }
                System.out.println("RoboSheep next location: " + nextLocationRoboSheep);

//                System.out.println("Steps: " + i);
//                System.out.println("RoboSheep last 10 step: " + roboSheepCoordinatesStore.receiveLastTenLocation());
//                System.out.println("RoboSheep previous location: " + previousLocationRoboSheep);
//                System.out.println("RoboSheep location: " + locationRoboSheep);
//                System.out.println(neighbourLawnFields);
//                System.out.println(neighbourMowedFields);
//                System.out.println("RoboSheep next location: " + nextLocationRoboSheep);

                printMapFromCoordinatesStore(
                        getGardenWidth(),
                        getGardenLength(),
                        locationRoboSheep,
                        locationCharger,
                        roboSheepCoordinatesStore,
                        coordinatesLawn);
                mowerUnit.mow(roboSheepCoordinatesStore, nextLocationRoboSheep, coordinatesLawn);
                battery.saveChargeLevelAfterMovement();
                keepDistanceBetweenScreenshots(3);
            } else {
                System.out.println("RoboSheep need to go back to charge.");
                break; //TODO sheep need to go back to the charge
            }
            if (Objects.equals(locationRoboSheep, locationCharger) && coordinatesLawn.getCoordinates().isEmpty()){
                break;
            }
        }
    }

}
