package evosoft;

import java.util.Collections;
import java.util.Objects;

import static evosoft.GardenMapImporter.*;
import static evosoft.ScreenPrinter.*;

public class WaterFall {
    private static final int MOVEMENTS_MAX = 1000000;
    //    private static final int MOVEMENTS_FAST = 196; // situation at left bottom corner
//    private static final int MOVEMENTS_FAST = 355; // situation in the center - left garden area
    private static final int MOVEMENTS_FAST = 388; // situation at right upper corner - left garden area
    //    private static final int MOVEMENTS_FAST = 1173;
    private static final int PERIMETER_MIN = 8;


    public void start() { //TODO not encapsulated
        String fileNameGardenMap = "garden_B";
        Battery battery = new Battery();
        MowerUnit mowerUnit = new MowerUnit();
        SearchingUnit searchingUnit = new SearchingUnit();
        RoboSheepCoordinatesStore roboSheepCoordinatesStore = new RoboSheepCoordinatesStore();
        Long locationCharger;
        Long locationRoboSheep;
        Long locationRoboSheepNext;
        Long locationRoboSheepPrevious;
        Long locationRoboSheepNextToPerimeter;
        Long nearestLawnField;
        Long nearestPerimeterFieldToRoboSheep;
        Long nearestPerimeterFieldToNearestLawn;
        CoordinateDataStore perimeterWay = new CoordinateDataStore();
        CoordinateDataStore lawnFields;
        CoordinateDataStore perimeterSegment;
        CoordinateDataStore neighbourLawnFields;
        CoordinateDataStore neighbourMowedFields;
        CoordinateDataStore nearestPerimeterFieldsToRoboSheep;

        lawnFields = importGardenMap(roboSheepCoordinatesStore, fileNameGardenMap);
        locationCharger = roboSheepCoordinatesStore.receiveLastLocation();
        keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, 0);

        for (int i = 0; i < MOVEMENTS_MAX; i++) {
            if (!battery.needCharge()) {
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                if (!perimeterWay.getCoordinates().contains(locationRoboSheep) &&
                        MOVEMENTS_FAST > perimeterWay.getCoordinates().size()) {
                    perimeterWay.addConvertedCoordinates(locationRoboSheep);
                }
                locationRoboSheepPrevious = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                neighbourLawnFields = searchingUnit.findAndSortNeighbourTypeFields(
                        "Lawn fields around RoboSheep", locationRoboSheepPrevious,
                        locationRoboSheep, lawnFields);
                neighbourMowedFields = searchingUnit.findAndSortNeighbourTypeFields(
                        "Mowed fields around RoboSheep", locationRoboSheepPrevious,
                        locationRoboSheep, roboSheepCoordinatesStore);
                locationRoboSheepNext = neighbourLawnFields.getCoordinates().size() > 0 ?
                        neighbourLawnFields.getCoordinates().get(0) :
                        neighbourMowedFields.getCoordinates().get(0);

                if (Collections.frequency(roboSheepCoordinatesStore.receiveLastTenLocation(), locationRoboSheep) > 2) {
                    nearestLawnField = lawnFields.getCoordinates().isEmpty() ?
                            locationCharger :
                            searchingUnit.findNearestFields(locationRoboSheep, lawnFields);
                    nearestPerimeterFieldToNearestLawn = searchingUnit.findNearestFields(nearestLawnField, perimeterWay);
                    perimeterSegment = searchingUnit.receivePerimeterSegment(
                            perimeterWay, locationRoboSheep, nearestPerimeterFieldToNearestLawn);
                    for (int j = 1; j <= perimeterSegment.getCoordinates().size(); j++) {
                        locationRoboSheepPrevious =
                                roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                        locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                        nearestLawnField = searchingUnit.findNearestFields(locationRoboSheep, lawnFields);
                        nearestPerimeterFieldsToRoboSheep = searchingUnit.findAndSortNeighbourTypeFields(
                                "nearestPerimeterFieldsToRoboSheep", locationRoboSheepPrevious,
                                locationRoboSheep, perimeterWay);
                        nearestPerimeterFieldToRoboSheep = searchingUnit.findNearestFields(
                                locationRoboSheep, nearestPerimeterFieldsToRoboSheep);
                        nearestPerimeterFieldToNearestLawn = searchingUnit.findNearestFields(
                                nearestLawnField, perimeterWay);
                        neighbourMowedFields = searchingUnit.findAndSortNeighbourTypeFields(
                                "neighbourMowedFields", locationRoboSheepPrevious,
                                locationRoboSheep, roboSheepCoordinatesStore);
                        locationRoboSheepNextToPerimeter = searchingUnit.findNearestFields(
                                nearestPerimeterFieldToRoboSheep,
                                neighbourMowedFields
                        );
                        if (!perimeterWay.getCoordinates().contains(locationRoboSheep)) {
                            locationRoboSheepNext = locationRoboSheepNextToPerimeter;
                        } else {
                            perimeterSegment = searchingUnit.receivePerimeterSegment(
                                    perimeterWay, locationRoboSheep, nearestPerimeterFieldToNearestLawn
                            );
                            for (int k = 0; k < perimeterSegment.getCoordinates().size(); k++) {
                                locationRoboSheepNext = perimeterWay.getCoordinates().get(k);
                                printStepsInLoop(i, j, k);
                                battery.printBatteryLevel();
                                printLocations(locationRoboSheepPrevious, locationRoboSheep, locationRoboSheepNext);
                                printCoordinates(roboSheepCoordinatesStore, neighbourLawnFields, neighbourMowedFields);
                                printPerimeterSegment(perimeterSegment);
                                printMapFromCoordinatesStore(
                                        getGardenWidth(), getGardenLength(), locationRoboSheep,
                                        locationCharger, roboSheepCoordinatesStore, lawnFields
                                );
                                mowerUnit.mow(roboSheepCoordinatesStore, locationRoboSheepNext, lawnFields);
                                battery.saveChargeLevelAfterMovement();
                                keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
                            }
                        }
                        printStepsInLoop(i, j, 0);
                        battery.printBatteryLevel();
                        printLocations(locationRoboSheepPrevious, locationRoboSheep, locationRoboSheepNext);
                        printCoordinates(roboSheepCoordinatesStore, neighbourLawnFields, neighbourMowedFields);
                        printPerimeterSegment(perimeterSegment);
                        printMapFromCoordinatesStore(getGardenWidth(), getGardenLength(), locationRoboSheep,
                                locationCharger, roboSheepCoordinatesStore, lawnFields
                        );
                        mowerUnit.mow(roboSheepCoordinatesStore, locationRoboSheepNext, lawnFields);
                        battery.saveChargeLevelAfterMovement();
                        keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
                    }
                }

                printStepsInLoop(i, 0, 0);
                battery.printBatteryLevel();
                printLocations(locationRoboSheepPrevious, locationRoboSheep, locationRoboSheepNext);
                printCoordinates(roboSheepCoordinatesStore, neighbourLawnFields, neighbourMowedFields);
                printMapFromCoordinatesStore(getGardenWidth(), getGardenLength(), locationRoboSheep,
                        locationCharger, roboSheepCoordinatesStore, lawnFields
                );
                mowerUnit.mow(roboSheepCoordinatesStore, locationRoboSheepNext, lawnFields);
                battery.saveChargeLevelAfterMovement();
                keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
            } else {
                System.out.println("RoboSheep need to go back to charge.");
                break; //TODO sheep need to go back to the charge
            }
            if (Objects.equals(locationRoboSheep, locationCharger) && lawnFields.getCoordinates().isEmpty()) {
                break;
            }
        }

    }


}
