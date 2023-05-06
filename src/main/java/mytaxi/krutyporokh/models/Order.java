package mytaxi.krutyporokh.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long clientId;
    private Long driverId;

    @NotBlank(message = "Booking date and time cannot be empty")
    private String bookingDateTime;

    @NotBlank(message = "Pickup address cannot be empty")
    @Size(max = 100, message = "Pickup address must be less than or equal to 100 characters")
    private String pickupAddress;

    @NotBlank(message = "Destination address cannot be empty")
    @Size(max = 100, message = "Destination address must be less than or equal to 100 characters")
    private String destinationAddress;

    @Size(min=2, max = 100, message = "Passenger name must be less than or equal to 100 characters")
    private String passengerName;

    @Pattern(regexp = "^\\+380[0-9]{9}$", message = "Invalid phone number, keep format +380-xxx-xxx-xxxx")
    private String passengerPhoneNumber;

    @Size(max = 300, message = "Booking notes must be less than or equal to 300 characters")
    private String bookingNotes;

    private PaymentType paymentType;

    private boolean payWithBonuses;

    private OrderStatus orderStatus;
}
