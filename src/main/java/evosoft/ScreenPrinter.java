package evosoft;

public class ScreenPrinter {

    private static final long TIME_OUT = 600;

    public static void waitNextScreen() {
        try {
            Thread.sleep(TIME_OUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
