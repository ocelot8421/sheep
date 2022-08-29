package evosoft;

import java.util.Collections;
import java.util.List;
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
        SearchingUnit searchingUnit = new SearchingUnit("The searching unit of RoboSheep");
        RoboSheepCoordinatesStore roboSheepCoordinatesStore = new RoboSheepCoordinatesStore("The way of RoboSheep");
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
        CoordinateDataStore lawnFields;
        CoordinateDataStore neighbourLawnFields;
        CoordinateDataStore neighbourMowedFields;
        CoordinateDataStore neighbourLawnFieldsCharger;
        CoordinateDataStore nearestPerimeterFieldsToRoboSheep;
        lawnFields = importGardenMap(roboSheepCoordinatesStore, fileNameGardenMap);
        locationCharger = roboSheepCoordinatesStore.receiveLastLocation();
        neighbourLawnFieldsCharger = searchingUnit.findAndSortNeighbourTypeFields(
                "Lawn fileds around charger", locationCharger, locationCharger, lawnFields);
        System.out.println("neighbourLawnFieldsCharger:" + neighbourLawnFieldsCharger);

        keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, 0);

        for (int i = 0; i < MOVEMENTS_MAX; i++) {
            if (!battery.needCharge()) {
                locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                perimeterWay = roboSheepCoordinatesStore;
                locationRoboSheepPrevious = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                neighbourLawnFields = searchingUnit.findAndSortNeighbourTypeFields(
                        "Lawn fields around RoboSheep", locationRoboSheepPrevious,
                        locationRoboSheep, lawnFields);
                nearestLawnField = lawnFields.getCoordinates().isEmpty() ?
                        locationCharger :
                        searchingUnit.findNearestField(locationRoboSheep, lawnFields);
                neighbourMowedFields = searchingUnit.findAndSortNeighbourTypeFields(
                        "Mowed fields around RoboSheep", locationRoboSheepPrevious,
                        locationRoboSheep, roboSheepCoordinatesStore);
                locationRoboSheepNextToNearestLawn = searchingUnit.findNearestField(nearestLawnField, neighbourMowedFields);
                locationRoboSheepNext = neighbourLawnFields.getCoordinates().size() > 0 ?
                        neighbourLawnFields.getCoordinates().get(0) :
                        locationRoboSheepNextToNearestLawn;
                boolean isInLoop =
                        Collections.frequency(roboSheepCoordinatesStore.receiveLastTenLocation(), locationRoboSheep) > 2;
                if (isInLoop) {
//                    CoordinateDataStore perimeterWayCorrected =
//                            searchingUnit.receiveCorrectedPerimeterWay(perimeterWay, neighbourLawnFieldsCharger.getCoordinates().get(2)); //TODO need test
                    List<Long> perimeterWayCoordinates = perimeterWay.getCoordinates();
                    List<Long> perimeterWayCorrected = searchingUnit.findLineSegment(
                            locationCharger, neighbourLawnFieldsCharger.getCoordinates().get(2), perimeterWayCoordinates //TODO get(2) need to be tested
                    );
                    nearestPerimeterFieldToRoboSheep =
                            searchingUnit.findNearestField(locationRoboSheep, new CoordinateDataStore(perimeterWayCorrected));
                    nearestPerimeterFieldTofNearestLawn =
                            searchingUnit.findNearestField(nearestLawnField, new CoordinateDataStore(perimeterWayCorrected));
                    locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                    List<Long> detour = searchingUnit.findLineSegment(
                            nearestPerimeterFieldToRoboSheep, nearestPerimeterFieldTofNearestLawn, perimeterWayCorrected);
                    if (detour.contains(locationRoboSheep)) {
                        for (int j = 0; j < detour.size(); j++) {
                            locationRoboSheepPrevious = roboSheepCoordinatesStore.receivePenultimateLocation(locationCharger);
                            locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                            locationRoboSheepNext = detour.get(j);
                            printStepsInLoop(i, j, 0);
                            battery.printBatteryLevel();
                            printLocations(locationRoboSheepPrevious, locationRoboSheep, locationRoboSheepNext);
                            printCoordinates(roboSheepCoordinatesStore, neighbourLawnFields, neighbourMowedFields);
                            System.out.println(detour);
                            printMapFromCoordinatesStore(getGardenWidth(), getGardenLength(), locationRoboSheep,
                                    locationCharger, roboSheepCoordinatesStore, lawnFields);
                            mowerUnit.mow(roboSheepCoordinatesStore, locationRoboSheepNext, lawnFields);
                            battery.saveChargeLevelAfterMovement();
                            keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
                        }
                    } else {
                        while (!perimeterWayCorrected.contains(locationRoboSheep)) {
                            locationRoboSheep = roboSheepCoordinatesStore.receiveLastLocation();
                            nearestPerimeterFieldToRoboSheep =
                                    searchingUnit.findNearestField(locationRoboSheep, new CoordinateDataStore(perimeterWayCorrected));
                            neighbourMowedFields = searchingUnit.findAndSortNeighbourTypeFields(
                                    "Mowed fields around RoboSheep", locationRoboSheepPrevious,
                                    locationRoboSheep, roboSheepCoordinatesStore);
                            locationRoboSheepNextToPerimeter =
                                    searchingUnit.findNearestField(nearestPerimeterFieldToRoboSheep, neighbourMowedFields);
                            locationRoboSheepNext = locationRoboSheepNextToPerimeter;
                            printStepsInLoop(i, 0, 0);
                            battery.printBatteryLevel();
                            printLocations(locationRoboSheepPrevious, locationRoboSheep, locationRoboSheepNext);
                            printCoordinates(roboSheepCoordinatesStore, neighbourLawnFields, neighbourMowedFields);
                            printMapFromCoordinatesStore(getGardenWidth(), getGardenLength(), locationRoboSheep,
                                    locationCharger, roboSheepCoordinatesStore, lawnFields);
                            mowerUnit.mow(roboSheepCoordinatesStore, locationRoboSheepNext, lawnFields);
                            battery.saveChargeLevelAfterMovement();
                            keepDistanceBetweenScreenshots(3, MOVEMENTS_FAST, i);
                        }
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
//                boolean isInBiggerLoop =
//                        Collections.frequency(roboSheepCoordinatesStore.receiveLastNCoordinates(20), locationRoboSheep) > 4;
//                if (isInBiggerLoop) {
//
//                }
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
