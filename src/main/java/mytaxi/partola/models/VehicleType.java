package mytaxi.partola.models;

public enum VehicleType {
    SEDAN("SEDAN"),
    HATCHBACK("HATCHBACK"),
    MINIVAN("MINIVAN"),
    MINIBUS("MINIBUS");
    private final String vehicleType;


    VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getValue(){
        return vehicleType;
    }
}
