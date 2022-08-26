package evosoft;

public class ScreenPrinter {

    private static final long TIME_OUT = 600;

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
