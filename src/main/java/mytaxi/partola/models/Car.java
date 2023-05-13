package mytaxi.partola.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Car {
    private long carId;
    private String licensePlate;
    private String model;
    private String color;
    private CarClass carClass;
    private VehicleType vehicleType;
}
