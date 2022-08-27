package evosoft;

public class ScreenPrinter {
//    private static final long TIME_OUT = 600;
//    private static final long TIME_OUT = 300;
    private static final long TIME_OUT = 10;

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
    public static void keepDistanceBetweenScreenshots(int emptyRowBetweenScreenshots) {
        System.out.println(new String(new char[emptyRowBetweenScreenshots]).replace("\0", "\r\n"));
        try {
            Thread.sleep(TIME_OUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
