package evosoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class GardenMapImporter {
    private static final Path mapPath = Path.of("src/main/resources/gardenMaps"); //make not static because of there can be more than one garden that use the same roboSheep
    private static long gardenWidth;
    private static long gardenLength;

    public static CoordinateDataStore importGardenMap(RoboSheepCoordinatesStore roboSheepCoordinatesStore) {
        String nameFile = "garden_A";
        CoordinateDataStore coordinatesLawn = new CoordinateDataStore();
        coordinatesLawn.setName("Coordinates of lawn fields");
        try (BufferedReader bufferedReader = Files.newBufferedReader(mapPath.resolve(nameFile))) {
            String line;
            long coordinateY = 0;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                coordinateY += 1;
                String[] fieldsOfRowArray = line.split("");
                gardenWidth = fieldsOfRowArray.length;
                for (int index = 0; index < gardenWidth; index++) {
                    if (Objects.equals(fieldsOfRowArray[index], "i")) {
                        long coordinateXlawn = index + 1;
                        coordinatesLawn.convertThenAddCoordinates(coordinateY, coordinateXlawn);
                    }
                    if (Objects.equals(fieldsOfRowArray[index], "X")) {
                        long coordinateXsheep = index + 1;
                        roboSheepCoordinatesStore.convertThenAddCoordinates(coordinateY, coordinateXsheep);
                    }
                }
                gardenLength = coordinateY;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinatesLawn;
    }

    public static long getGardenWidth() {
        return gardenWidth;
    }

    public static long getGardenLength() {
        return gardenLength;
    }
}
