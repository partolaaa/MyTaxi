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

    public String show () {
        return vehicleType
                .substring(0, 1).toUpperCase()
                + vehicleType.substring(1).toLowerCase();

    }
}
