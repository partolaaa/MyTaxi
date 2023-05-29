package mytaxi.partola.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Ivan Partola
 * @date 04.05.2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends CustomUser {
    private long clientId;
    @NotBlank(message = "Phone number is required")
    @Size(max = 13, message = "Invalid phone number, keep format 380xxxxxxxxxx")
    @Pattern(regexp = "^380[0-9]{9}$", message = "Invalid phone number, keep format 380xxxxxxxxxx")
    private String phoneNumber;
    private float rating;
    private float bonusAmount;
    private boolean hasActiveOrder;
    private int numberOfRatings;
    private int totalRatings;
}
