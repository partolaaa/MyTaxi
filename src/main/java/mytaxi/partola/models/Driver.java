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
public class Driver extends CustomUser {
    private Long driverId;
    private String licenseNumber;
    private float rating;
    private String phoneNumber;
    private boolean isBusy;
    private long carId;
    private int numberOfRatings;
    private int totalRatings;
}
