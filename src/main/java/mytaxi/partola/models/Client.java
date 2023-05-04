package mytaxi.partola.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Ivan Partola
 * @date 04.05.2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends CustomUser {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    private int rating;
    private float bonusAmount;
}
