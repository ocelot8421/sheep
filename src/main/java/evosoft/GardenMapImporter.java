package evosoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static evosoft.CoordinateHandler.findCoordinateX;

public class GardenMapImporter {
    private static final Path mapPath = Path.of("src/main/resources/gardenMaps");


    public static void importGardenMap() {
        String nameFile = "garden_A";
        try (BufferedReader bufferedReader = Files.newBufferedReader(mapPath.resolve(nameFile))
//             ; BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of("src/main/resources/" + nameFile))
        ) {
            String line;
            CoordinateDataStore coordinatesLawn = new CoordinateDataStore();
            coordinatesLawn.setName("Coordinates of lawn fields");
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                long coordinateX = findCoordinateX(line);
                coordinatesLawn.addCoordinate(coordinateX);
//                bufferedWriter.write(line);
//                bufferedWriter.newLine();
            }
        System.out.println(coordinatesLawn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
