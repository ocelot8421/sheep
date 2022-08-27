package evosoft;


import java.util.ArrayList;
import java.util.List;

public class RoboSheepCoordinatesStore extends CoordinateDataStore {

    public Long receiveLastLocation() {
        List<Long> locationList = super.getCoordinates();
        return locationList.get(locationList.size() - 1);
    }

    public List<Long> receiveLastTenLocation() {
        List<Long> locationList = super.getCoordinates();
        List<Long> last5Location = new ArrayList<>();
        int lastStepsLimit = Math.min(locationList.size(), 10);
        for (int i = 0; i < lastStepsLimit; i++) {
            last5Location.add(locationList.get(locationList.size() - lastStepsLimit + i));
        }
        return last5Location;
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
