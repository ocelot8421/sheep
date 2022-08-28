package evosoft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static evosoft.GardenMapImporter.*;
import static evosoft.ScreenPrinter.*;

public class WaterFall {
    private static final int MOVEMENTS_MAX = 1000000;
    private static final int MOVEMENTS_FAST = 1173;
    private static final int PERIMETER_MIN = 8;


    public void start() {
        String nameFile = "garden_B";

        RoboSheepBattery battery = new RoboSheepBattery();
        RoboSheepCoordinatesStore roboSheepCoordinatesStore = new RoboSheepCoordinatesStore();
        roboSheepCoordinatesStore.setName("Coordinates of RoboSheep");
        RoboSheepSearchingUnit searchingUnit = new RoboSheepSearchingUnit();
        RoboSheepMowerUnit mowerUnit = new RoboSheepMowerUnit();

        CoordinateDataStore coordinatesLawn = importGardenMap(roboSheepCoordinatesStore, nameFile);
        keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, 0);

        Long locationCharger = roboSheepCoordinatesStore.receiveLastLocation();
        Long locationRoboSheep;
        Long nextLocationRoboSheep;
        Long previousLocationRoboSheep;
        CoordinateDataStore chargerCoordinates =
                searchingUnit.findAndSortNeighbourTypeFields(
                        locationCharger,
                        locationCharger,
                        coordinatesLawn);
        List<Long> perimeterWay = new ArrayList<>();
        List<Integer> perimeterLength = new ArrayList<>();

        for (int i = 0; i < MOVEMENTS_MAX; i++) {
            if (!battery.needCharge()) {
                previousLocationRoboSheep = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                chargerCoordinates.setName("Neighbour fields of charger");
                if (!perimeterWay.contains(locationRoboSheep) &&
                        MOVEMENTS_FAST > perimeterWay.size()
                ) {
                    perimeterWay.add(locationRoboSheep);
                } else {
                    perimeterLength.add(perimeterWay.size());
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
                    if (Collections.frequency(
                            roboSheepCoordinatesStore.receiveLastTenLocation(), locationRoboSheep) > 3
                    ) {
                        int perimeterQuarter = perimeterLength.get(0) / 4;
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
                            long stepVector = locationRoboSheep - previousLocationRoboSheep;
                            CoordinateDataStore coordinatesPerimeter = new CoordinateDataStore();
                            coordinatesPerimeter.getCoordinates().addAll(perimeterWay);

                            Long nearestPerimeterField = searchingUnit.findNearestFields(
                                    locationRoboSheep, coordinatesPerimeter);
                            nextLocationRoboSheep = (Collections.frequency(
                                    roboSheepCoordinatesStore.receiveLastTenLocation(), locationRoboSheep) > 2
                            ) ? searchingUnit.findNearestFields(nearestPerimeterField, neighbourMowedFields)
                                    : neighbourMowedFields.getCoordinates().get(1);
                            i += j;
                            System.out.println("Steps in nested loop: " + j);
                            System.out.println("Steps: " + i);
                            System.out.println("RoboSheep last 10 step: " + roboSheepCoordinatesStore.receiveLastTenLocation());
                            System.out.println("RoboSheep previous location: " + previousLocationRoboSheep);
                            System.out.println("RoboSheep location: " + locationRoboSheep);
                            System.out.println("stepVector: " + stepVector);
                            System.out.println(neighbourLawnFields);
                            System.out.println(neighbourMowedFields);
                            System.out.println("nearestPerimeterField: " + nearestPerimeterField);
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
                            keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
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
                System.out.println("stepVector: " + (locationRoboSheep - previousLocationRoboSheep));
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
                keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
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
