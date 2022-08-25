package evosoft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static evosoft.CoordinateReader.findCoordinateX;

public class GardenMapImporter {
    private static final Path mapPath = Path.of("src/main/resources/gardenMaps");
    static String nameFile = "garden_A";


    public static void importGardenMap() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(mapPath.resolve(nameFile))
//             ; BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of("src/main/resources/" + nameFile))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                findCoordinateX();
//                bufferedWriter.write(line);
//                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
