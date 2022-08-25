package evosoft;

import java.io.IOException;

import static evosoft.GardenMapImporter.importGardenMap;
import static evosoft.ScreenPrinter.waitNextScreen;

public class Main {
    public static void main(String[] args) throws IOException {
        int numberOfMoves = 5;
        int spaceBetweenScreenshots = 3;

        for (int i = 0; i < numberOfMoves; i++) {
            importGardenMap();
            System.out.println(new String(new char[spaceBetweenScreenshots]).replace("\0", "\r\n"));
            waitNextScreen();
        }
    }

}
