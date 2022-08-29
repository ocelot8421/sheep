package evosoft;


import java.util.ArrayList;
import java.util.List;

public class RoboSheepCoordinatesStore extends CoordinateDataStore {

    public RoboSheepCoordinatesStore() {
    }

    public RoboSheepCoordinatesStore(String name) {
        super(name);
    }

    public Long receiveLastLocation() {
        List<Long> locationList = super.getCoordinates();
        return locationList.get(locationList.size() - 1);
    }

    public List<Long> receiveLastTenLocation() {
        List<Long> locationList = super.getCoordinates();
        List<Long> last10Location = new ArrayList<>();
        int lastStepsLimit = Math.min(locationList.size(), 10);
        for (int i = 0; i < lastStepsLimit; i++) {
            last10Location.add(locationList.get(locationList.size() - lastStepsLimit + i));
        }
        return last10Location;
    }

    public Long receivePenultimateLocation(Long locationCharger) {
        int size = receiveLastTenLocation().size();
        if (size < 2) {
            return locationCharger;
        } else {
            return receiveLastTenLocation().get(size - 2);
        }
    }
}
