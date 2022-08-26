package evosoft;

import java.util.ArrayList;
import java.util.List;

public class CoordinateDataStore {
    private String name = "Unnamed coordinate data store";
    private List<Long> coordinates = new ArrayList<>();

    public void addCoordinate(long newCoordinate){
        coordinates.add(newCoordinate);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CoordinateDataStore{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
