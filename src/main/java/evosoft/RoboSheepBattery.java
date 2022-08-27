package evosoft;

public class RoboSheepBattery {
    private int batteryLevel = 100000;
    private int minBatteryLevel = 100; // TODO It depends on the distance


    public boolean needCharge(){
        boolean needCharge = true;
        if (batteryLevel > minBatteryLevel){
            System.out.println("Level of charge: OK (" + batteryLevel + ")");
            needCharge = false;
        } else {
            System.out.println("Level of charge: LOW (" + batteryLevel + ")");
        }
        return needCharge;
    }

    //Input should be a data from sensor. This model is only estimated.
    public void saveChargeLevelAfterMovement(){
        batteryLevel -= 1;
    }
}
