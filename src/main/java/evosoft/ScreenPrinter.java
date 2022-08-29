package evosoft;

public class ScreenPrinter {
//    private static final long TIME_MEDIUM = 600;
    private static final long TIME_MEDIUM = 150;
//    private static final long TIME_MEDIUM = 50;
private static final long TIME_FAST = 150;
    //    private static final long TIME_FAST = 50;
//    private static final long TIME_FAST = 0;

    public static void printMapFromCoordinatesStore(
            long gardenWidth, long gardenLength,
            Long locationRoboSheep, Long locationCharger,
            CoordinateDataStore roboSheepCoordinatesStore,
            CoordinateDataStore coordinatesLawn
    ) {
        for (int j = 1; j <= gardenLength; j++) {
            for (int i = 1; i <= gardenWidth; i++) {
                long coordinate = j * 1000000L + i;
                if (coordinate == locationRoboSheep) {
                    System.out.print("\uD83D\uDC11");
                } else if (coordinate == locationCharger) {
                    System.out.print("O");
                } else if (roboSheepCoordinatesStore.getCoordinates().contains(coordinate)) {
                    System.out.print("_");
                } else if (coordinatesLawn.getCoordinates().contains(coordinate)) {
                    System.out.print("i");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //after printed characters of map it makes some empty rows in IntelliJ console
    public static void keepDistanceBetweenScreenshots(int emptyRowBetweenScreenshots, int movementsFast, int stepActual) {
        System.out.println(new String(new char[emptyRowBetweenScreenshots]).replace("\0", "\r\n"));
        try {
            long timeOut = 0;
            if (stepActual < movementsFast) {
                timeOut = TIME_FAST;
            } else {
                timeOut = TIME_MEDIUM;
            }
            Thread.sleep(timeOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printStepsInLoop(int iLoop, int jLoop, int kLoop) {
        System.out.println("----------------------------------------");
        System.out.println("Steaps in a loop: ");
        System.out.println(".................");
        System.out.println("Steps in i loop: " + iLoop);
        System.out.println("Steps in j loop: " + jLoop);
        System.out.println("Steps sum      : " + (iLoop + jLoop));
        System.out.println("----------------------------------------");
    }

    public static void printLocations(Long previousLocationRoboSheep, Long locationRoboSheep, Long nextLocationRoboSheep) {
        System.out.println("Locations: ");
        System.out.println("...........");
        System.out.println("RoboSheep previous location: " + previousLocationRoboSheep);
        System.out.println("RoboSheep location         : " + locationRoboSheep);
        System.out.println("stepVector                 : " + (locationRoboSheep - previousLocationRoboSheep));
        System.out.println("RoboSheep next location    : " + nextLocationRoboSheep);
        System.out.println("----------------------------------------");
    }

    public static void printCoordinates(RoboSheepCoordinatesStore roboSheepCoordinatesStore, //TODO too many parameters!
                                        CoordinateDataStore neighbourLawnFields,
                                        CoordinateDataStore neighbourMowedFields
    ) {
        System.out.println("Coordinates of an area: ");
        System.out.println("........................");
        System.out.println("RoboSheep last 10 step: " + roboSheepCoordinatesStore.receiveLastTenLocation());
        System.out.println(neighbourLawnFields);
        System.out.println(neighbourMowedFields);
        System.out.println("----------------------------------------");
    }


    public static void printPerimeterSegment(CoordinateDataStore perimeterSegment) {
        System.out.print("Perimeter segment: ");
        System.out.println(perimeterSegment.receiveFirstNCoordinates(3) + "...  ..." + perimeterSegment.receiveLastNCoordinates(3));
        System.out.println("----------------------------------------");
    }
}
