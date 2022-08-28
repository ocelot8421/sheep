package evosoft;

public class Battery {
    private int batteryLevel = 100000;
    private int minBatteryLevel = 100; // TODO It depends on the distance


    public boolean needCharge(){
        boolean needCharge = true;
        if (batteryLevel > minBatteryLevel){
            needCharge = false;
        } else {
        }
        return needCharge;
    }

    public void printBatteryLevel(){
        String s = needCharge() ? "LOW" : "OK";
            System.out.println("Level of charge: " + s + " (" + batteryLevel + ")");
        System.out.println("----------------------------------------");
    }

    //Input should be a data from sensor. This model is only estimated.
    public void saveChargeLevelAfterMovement(){
        batteryLevel -= 1;
    }
}
