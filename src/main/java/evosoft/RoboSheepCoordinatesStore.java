package evosoft;


import java.util.ArrayList;
import java.util.List;

public class RoboSheepCoordinatesStore extends CoordinateDataStore {

    public Long receiveLastLocation() {
        List<Long> locationList = super.getCoordinates();
        return locationList.get(locationList.size() - 1);
    }

    public List<Long> receiveLast5Location() {
        List<Long> locationList = super.getCoordinates();
        List<Long> last5Location = new ArrayList<>();
        int lastStepsLimit = Math.min(locationList.size(), 5);
        for (int i = 0; i < lastStepsLimit; i++) {
                last5Location.add(locationList.get(locationList.size() - lastStepsLimit + i));
        }
        return last5Location;
    }
}
