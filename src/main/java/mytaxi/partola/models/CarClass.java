package mytaxi.partola.models;

/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
public enum CarClass {
    BUSINESS("BUSINESS"),
    ECONOMY("ECONOMY"),
    ;

    private final String carClass;

    CarClass(String carClass) {
        this.carClass = carClass;
    }

    public String getValue () {
        return carClass;
    }
}
