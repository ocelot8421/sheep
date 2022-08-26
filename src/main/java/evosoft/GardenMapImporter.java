package evosoft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class GardenMapImporter {
    private static final Path mapPath = Path.of("src/main/resources/gardenMaps");

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
                for (int index = 0; index < fieldsOfRowArray.length; index++) {
                    if (Objects.equals(fieldsOfRowArray[index], "i")) {
                        long coordinateXlawn = index + 1;
                        coordinatesLawn.addConvertedCoordinate(coordinateY, coordinateXlawn);
                    }
                    if (Objects.equals(fieldsOfRowArray[index], "X")) {
                        long coordinateXsheep = index + 1;
                        roboSheepCoordinatesStore.addConvertedCoordinate(coordinateY, coordinateXsheep);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinatesLawn;
    }
}
