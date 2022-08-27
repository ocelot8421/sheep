package evosoft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static evosoft.GardenMapImporter.*;
import static evosoft.ScreenPrinter.*;

public class WaterFall {
    private static final int MOVEMENTS_MAX = 1000000;

    public void start() {
        String nameFile = "garden_B";

        RoboSheepBattery battery = new RoboSheepBattery();
        RoboSheepCoordinatesStore roboSheepCoordinatesStore = new RoboSheepCoordinatesStore();
        roboSheepCoordinatesStore.setName("Coordinates of RoboSheep");
        RoboSheepSearchingUnit searchingUnit = new RoboSheepSearchingUnit();
        RoboSheepMowerUnit mowerUnit = new RoboSheepMowerUnit();

        CoordinateDataStore coordinatesLawn = importGardenMap(roboSheepCoordinatesStore, nameFile);
        keepDistanceBetweenScreenshots(3);

        Long locationCharger = roboSheepCoordinatesStore.receiveLastLocation();
        Long locationRoboSheep;
        Long nextLocationRoboSheep;
        Long previousLocationRoboSheep;
        CoordinateDataStore chargerCoordinates =
                searchingUnit.findAndSortNeighbourTypeFields(
                        locationCharger,
                        locationCharger,
                        coordinatesLawn);
        List<Integer> perimeter = new ArrayList<>();


        for (int i = 0; i < MOVEMENTS_MAX; i++) {
            if (!battery.needCharge()) {
                previousLocationRoboSheep = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                chargerCoordinates.setName("Neighbour fields of charger");
                if (chargerCoordinates.getCoordinates().contains(locationRoboSheep) &&
                        roboSheepCoordinatesStore.getCoordinates().size() > 8
                ) {
                    perimeter.add(roboSheepCoordinatesStore.getCoordinates().size());
                }
                CoordinateDataStore neighbourLawnFields =
                        searchingUnit.findAndSortNeighbourTypeFields(
                                previousLocationRoboSheep,
                                locationRoboSheep,
                                coordinatesLawn);
                neighbourLawnFields.setName("Neighbour  lawn fields");
                CoordinateDataStore neighbourMowedFields =
                        searchingUnit.findAndSortNeighbourTypeFields(
                                previousLocationRoboSheep,
                                locationRoboSheep,
                                roboSheepCoordinatesStore);
                neighbourMowedFields.setName("Neighbour mowed fields");
                if (neighbourLawnFields.getCoordinates().size() > 0) {
                    nextLocationRoboSheep = neighbourLawnFields.getCoordinates().get(0);
                } else {
                    Long nearestLawnField;
                    if (!coordinatesLawn.getCoordinates().isEmpty()) {
                        nearestLawnField = searchingUnit.findNearestFields(locationRoboSheep, coordinatesLawn);
                    } else {
                        nearestLawnField = locationCharger;
                    }
                    Long mowedFieldToNearestLawnFiled = searchingUnit
                            .findNearestFields(nearestLawnField, neighbourMowedFields);
                    nextLocationRoboSheep = mowedFieldToNearestLawnFiled;
                    long lastLocation9th = roboSheepCoordinatesStore.receiveLastTenLocation().get(8);
                    long lastLocation10th = roboSheepCoordinatesStore.receiveLastTenLocation().get(9);
                    if (lastLocation10th == lastLocation9th ||
                            Collections.frequency(
                                    roboSheepCoordinatesStore.receiveLastTenLocation(), lastLocation10th) > 2
                    ) {
                        int perimeterQuarter = perimeter.get(0) / 4;
                        for (int j = 1; j <= perimeterQuarter; j++) {
                            System.out.println(perimeterQuarter + "//------------");
                            previousLocationRoboSheep =
                                    roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                            locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                            neighbourMowedFields =
                                    searchingUnit.findAndSortNeighbourTypeFields(
                                            previousLocationRoboSheep,
                                            locationRoboSheep,
                                            roboSheepCoordinatesStore);
                            nextLocationRoboSheep = neighbourMowedFields.getCoordinates().get(0);
                            i += j;
                            System.out.println("Steps in nested loop: " + j);
                            System.out.println("Steps: " + i);
                            System.out.println("RoboSheep last 10 step: " + roboSheepCoordinatesStore.receiveLastTenLocation());
                            System.out.println("RoboSheep previous location: " + previousLocationRoboSheep);
                            System.out.println("RoboSheep location: " + locationRoboSheep);
                            System.out.println(neighbourLawnFields);
                            System.out.println(neighbourMowedFields);
                            System.out.println("RoboSheep next location: " + nextLocationRoboSheep);
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
                        }
                    }
                    System.out.println("Nearest lawn field: " + nearestLawnField);
                    System.out.println("Nearest mowed neighbour field to nearest lawn field: " +
                            mowedFieldToNearestLawnFiled);
                }
                System.out.println("Steps: " + i);
                System.out.println("RoboSheep last 10 step: " + roboSheepCoordinatesStore.receiveLastTenLocation());
                System.out.println("RoboSheep previous location: " + previousLocationRoboSheep);
                System.out.println("RoboSheep location: " + locationRoboSheep);
                System.out.println(neighbourLawnFields);
                System.out.println(neighbourMowedFields);
                System.out.println("RoboSheep next location: " + nextLocationRoboSheep);
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
            if (Objects.equals(locationRoboSheep, locationCharger) && coordinatesLawn.getCoordinates().isEmpty()) {
                break;
            }
        }

    }

}
