package evosoft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateHandler {

    public static Long findCoordinateX(String line, String markerLawn){
        long coordinateX = -1L;
        Pattern pattern = Pattern.compile(markerLawn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        boolean matchFound = matcher.find();
        if (matchFound) {
            coordinateX = matcher.start() + 1;
        }
        return coordinateX;
    }
}
