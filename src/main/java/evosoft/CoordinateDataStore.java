package evosoft;

import java.util.ArrayList;
import java.util.List;

public class CoordinateDataStore {
    private String name = "Unnamed coordinate data store";
    private List<Long> coordinates = new ArrayList<>();

    public CoordinateDataStore() {
    }

    public CoordinateDataStore(String name) {
        this.name = name;
    }

    //The column is marked by last 6 digits, other digits before that shows the number of row. The numbering start with 1 (not with 0)!
    public void convertThenAddCoordinates(long coordinateY, long coordinateX) {
        if (coordinateX >= 0) {
            coordinates.add(coordinateY * 1000000 + coordinateX);
        }
    }

    public List<Long> receiveFirstNCoordinates(int n) {
        int endIndex = coordinates.size() == 0 ? 0 : Math.min(n, coordinates.size() - 1);
        return coordinates.subList(0, endIndex);
    }

    public List<Long> receiveLastNCoordinates(int n) {
        int endIndex = coordinates.size() == 0 ? 0 : Math.max(n, coordinates.size() - 1);
        int startIndex = Math.max(endIndex - n, 0);
         return coordinates.subList(startIndex, endIndex);
    }

    public void addConvertedCoordinates(long coordinate) {
        coordinates.add(coordinate);
    }

    public void removeConvertedCoordinate(long coordinate) {
        coordinates.remove(coordinate);
    }

    public List<Long> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Long> coordinates) {
        this.coordinates = coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ": " + coordinates;
    }
}
