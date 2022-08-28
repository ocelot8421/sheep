package evosoft;

import java.util.Collections;
import java.util.Objects;

import static evosoft.GardenMapImporter.*;
import static evosoft.ScreenPrinter.*;

public class WaterFall {
    private static final int MOVEMENTS_MAX = 1000000;
    //    private static final int MOVEMENTS_FAST = 196; // situation at left bottom corner
//    private static final int MOVEMENTS_FAST = 355; // situation in the center - left garden area
//    private static final int MOVEMENTS_FAST = 388; // situation at right upper corner - left garden area
//    private static final int MOVEMENTS_FAST = 475; // situation at left upper corner - right garden area
    private static final int MOVEMENTS_FAST = 668; // situation at right side - right garden area
    //    private static final int MOVEMENTS_FAST = 708; // going back is not too perfect
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
        Long locationRoboSheepNextToNearestLawn;
        Long nearestLawnField;
        Long nearestPerimeterFieldToRoboSheep;
        Long nearestPerimeterFieldTofNearestLawn;
        CoordinateDataStore perimeterWay = new CoordinateDataStore("Perimeter way");
//        CoordinateDataStore perimeterWayCorrected = new CoordinateDataStore("Corrected perimeter way");
        CoordinateDataStore lawnFields;
        CoordinateDataStore neighbourLawnFields;
        CoordinateDataStore neighbourMowedFields;
        CoordinateDataStore neighbourMowedFieldsCharger;
        CoordinateDataStore nearestPerimeterFieldsToRoboSheep;

        lawnFields = importGardenMap(roboSheepCoordinatesStore, fileNameGardenMap);
        locationCharger = roboSheepCoordinatesStore.receiveLastLocation();
        keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, 0);

        for (int i = 0; i < MOVEMENTS_MAX; i++) {
            if (!battery.needCharge()) {
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                neighbourMowedFieldsCharger = searchingUnit.findAndSortNeighbourTypeFields(
                        "Moved fields around charger", locationCharger,
                        locationCharger, roboSheepCoordinatesStore);
                if (!perimeterWay.getCoordinates().contains(locationRoboSheep) &&
                        MOVEMENTS_FAST > perimeterWay.getCoordinates().size()) {
                    perimeterWay.addConvertedCoordinates(locationRoboSheep);
                }
                locationRoboSheepPrevious = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                neighbourLawnFields = searchingUnit.findAndSortNeighbourTypeFields(
                        "Lawn fields around RoboSheep", locationRoboSheepPrevious,
                        locationRoboSheep, lawnFields);
                nearestLawnField = lawnFields.getCoordinates().isEmpty() ?
                        locationCharger :
                        searchingUnit.findNearestFields(locationRoboSheep, lawnFields);
                neighbourMowedFields = searchingUnit.findAndSortNeighbourTypeFields(
                        "Mowed fields around RoboSheep", locationRoboSheepPrevious,
                        locationRoboSheep, roboSheepCoordinatesStore);
                locationRoboSheepNextToNearestLawn = searchingUnit.findNearestFields(nearestLawnField, neighbourMowedFields);
                locationRoboSheepNext = neighbourLawnFields.getCoordinates().size() > 0 ?
                        neighbourLawnFields.getCoordinates().get(0) :
                        locationRoboSheepNextToNearestLawn;
                boolean isInLoop =
                        Collections.frequency(roboSheepCoordinatesStore.receiveLastTenLocation(), locationRoboSheep) > 2;
                neighbourMowedFieldsCharger = searchingUnit.findAndSortNeighbourTypeFields(
                        "", locationCharger,
                        locationCharger, lawnFields);
                CoordinateDataStore perimeterWayCorrected = searchingUnit.receiveCorrectedPerimeterWay(perimeterWay, neighbourMowedFieldsCharger);
                nearestPerimeterFieldToRoboSheep =
                        searchingUnit.findNearestFields(locationRoboSheep, perimeterWayCorrected);
                nearestPerimeterFieldTofNearestLawn =
                        searchingUnit.findNearestFields(nearestLawnField, perimeterWayCorrected);
                if (isInLoop) {
                    CoordinateDataStore detour = searchingUnit.findDetour(
                            nearestPerimeterFieldToRoboSheep, nearestPerimeterFieldTofNearestLawn, perimeterWayCorrected);
                    for (int j = 0; j < detour.getCoordinates().size() - 1; j++) {
                        locationRoboSheep = detour.getCoordinates().get(j);
                        locationRoboSheepNext = detour.getCoordinates().get(j + 1);
                        printStepsInLoop(i, j, 0);
                        battery.printBatteryLevel();
                        printLocations(locationRoboSheepPrevious, locationRoboSheep, locationRoboSheepNext);
                        printCoordinates(roboSheepCoordinatesStore, neighbourLawnFields, neighbourMowedFields);
                        printMapFromCoordinatesStore(getGardenWidth(), getGardenLength(), locationRoboSheep,
                                locationCharger, roboSheepCoordinatesStore, lawnFields);

                        System.out.println("Detour:");
                        System.out.println(detour);

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
                        locationCharger, roboSheepCoordinatesStore, lawnFields);

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
