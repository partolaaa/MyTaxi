package mytaxi.partola.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUser {
    private long userId;

    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 100, message = "Name must be at most 100 characters and at least 2")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    //@Length(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    private String password;

    /*@NotBlank(message = "Phone number is required")
    //@Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;*/

    private boolean isBanned;
    private Role role;
}
