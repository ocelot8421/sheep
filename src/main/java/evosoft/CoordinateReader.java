package evosoft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateReader {
    public static void findCoordinateX(){
        Pattern pattern = Pattern.compile("x", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("AsteriXus");
        boolean matchFound = matcher.find();
        if (matchFound) {
            System.out.println(matcher.start());
        } else {
            System.out.println("Match not found");
        }
    }
}
