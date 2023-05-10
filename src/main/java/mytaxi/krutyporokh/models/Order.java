package mytaxi.krutyporokh.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mytaxi.krutyporokh.validation.groups.OrderForAnotherPerson;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long clientId;
    private Long driverId;
    private float price;

    @NotBlank(message = "Booking date and time cannot be empty")
    private String bookingDatetime;

    @NotBlank(message = "Pickup address cannot be empty")
    @Size(max = 100, message = "Pickup address must be less than or equal to 100 characters")
    private String pickupAddress;

    @NotBlank(message = "Destination address cannot be empty")
    @Size(max = 100, message = "Destination address must be less than or equal to 100 characters")
    private String destinationAddress;

    @Size(min=2, max = 100, message = "Passenger name must be less than or equal to 100 characters", groups = OrderForAnotherPerson.class)
    private String passengerName;

    // So strict, we don't need it
    //@Pattern(regexp = "^\\+380[0-9]{9}$", message = "Invalid phone number, keep format +380-xxx-xxx-xxxx", groups = OrderForAnotherPerson.class)
    private String passengerPhoneNumber;

    @Size(max = 300, message = "Booking notes must be less than or equal to 300 characters")
    private String bookingNotes;
    private PaymentType paymentType;

    private boolean payWithBonuses;

    private OrderStatus orderStatus;
}

