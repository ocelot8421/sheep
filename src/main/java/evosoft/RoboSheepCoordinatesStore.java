package evosoft;


import java.util.List;

public class RoboSheepCoordinatesStore extends CoordinateDataStore {

    public Long receiveLastLocation(){
        List<Long> locationList = super.getCoordinates();
        return locationList.get(locationList.size()-1);
    }

}
