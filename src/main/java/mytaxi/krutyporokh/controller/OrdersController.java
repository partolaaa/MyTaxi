package mytaxi.krutyporokh.controller;

import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.services.OrderManagementService;
import mytaxi.krutyporokh.services.OrderRatingService;
import mytaxi.krutyporokh.services.OrderStatusService;
import mytaxi.krutyporokh.validation.groups.OrderForAnotherPerson;
import mytaxi.krutyporokh.validation.groups.OrderForSelf;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.CarService;
import mytaxi.partola.services.ClientService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;

@Controller
public class OrdersController {
    private final Validator validator;
    private final ClientService clientService;
    private final CustomUserService customUserService;
    private final OrderManagementService orderManagementService;
    private final OrderStatusService orderStatusService;
    private final OrderRatingService orderRatingService;
    private final DriverService driverService;
    private final CarService carService;

    @Value("${googleMapsAPIKey}")
    private String googleMapsAPIKey;

    @Autowired
    public OrdersController(Validator validator, ClientService clientService, CustomUserService customUserService, OrderManagementService orderManagementService, OrderStatusService orderStatusService, OrderRatingService orderRatingService, DriverService driverService, CarService carService) {
        this.validator = validator;
        this.clientService = clientService;
        this.customUserService = customUserService;
        this.orderManagementService = orderManagementService;
        this.orderStatusService = orderStatusService;
        this.orderRatingService = orderRatingService;
        this.driverService = driverService;
        this.carService = carService;
    }

    @GetMapping("order")
    public String order (@ModelAttribute("order") Order order,
                         Model model) {
        model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);
        // getUsername() method returns email in our case

        CustomUser currentUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", currentUser);
        Client client = clientService.findClientById(currentUser.getUserId());
        model.addAttribute("client", client);
        return "client/order";
    }

    @PostMapping("/createNewOrder")
    public String  createNewOrder(@RequestParam(name = "order-for-another-person", required = false) boolean orderForAnotherPerson,
                                  @ModelAttribute("order") @Valid Order order,
                                  BindingResult bindingResult,
                                  Model model
                               ){

        // Validate with the selected group
        Set<ConstraintViolation<Order>> violations;
        if (orderForAnotherPerson) {
            violations = validator.validate(order, OrderForAnotherPerson.class);
        } else {
            violations = validator.validate(order, OrderForSelf.class);
        }

        // Processing validation results
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Order> violation : violations) {
                bindingResult.rejectValue(violation.getPropertyPath().toString(), null, violation.getMessage());
            }
        }

        CustomUser currentUser = customUserService.getCurrentUserFromSession().get();
        Client client = clientService.findClientById(currentUser.getUserId());

        // Check if client already has active orders
        if (client.isHasActiveOrder()) {
            bindingResult.rejectValue("orderStatus", null, "You already have an active order.");
        }

        if (order.getBookingDatetime() == null) {
            bindingResult.rejectValue("bookingDatetime", null, "Booking time cannot be empty.");
        }


        //Processing validation errors
        if(bindingResult.hasErrors()){
            //Re-adding Google Maps API key to avoid problems with map crash
            model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);

            model.addAttribute("user", currentUser);
            model.addAttribute("client", client);
            model.addAttribute("bonusesAmount", client.getBonusAmount());
            model.addAttribute("error", bindingResult.getAllErrors());
            return "client/order";
        }
        // If user pays with bonuses, we remove them from their account
        clientService.subtractBonuses(client.getClientId(), order);
        orderManagementService.createNewOrder(order, client);
        clientService.addBonusesByUserAndOrderPrice(currentUser, order.getPrice());
        clientService.setHasActiveOrderStatus(client, true);

        return "redirect:/my-orders";
    }

    @GetMapping("/order/{hash}")
    public String activeOrder(@PathVariable String hash,
                              Model model) {
        CustomUser currentUser = customUserService.getCurrentUserFromSession().get();
        Order currentOrder = orderManagementService.findOrderByHash(hash);

        // If this order is not order of current user, so we don't show it
        if (currentOrder.getClientId() != currentUser.getUserId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource.");
        }

        Driver driver = driverService.findDriverById(currentOrder.getDriverId());

        model.addAttribute("user", currentUser);
        model.addAttribute("driver", driver);
        model.addAttribute("order", currentOrder);
        model.addAttribute("car", carService.getCarByDriver(driver));
        return "client/activeOrder";
    }

    @PostMapping("/rateTheTrip")
    public ResponseEntity<?> ratedTrip (@RequestParam String hash,
                                        @RequestParam int rating) {
        orderRatingService.rateTrip(hash, rating);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{hash}/cancel")
    public ResponseEntity<Float> cancelOrder (@PathVariable String hash) {
        orderStatusService.cancelOrder(hash);
        float resultBonusesAmount = orderStatusService.subtractBonusesIfOrderWasCancelled(hash);
        clientService.setHasActiveOrderStatus(clientService.findClientById(orderManagementService.findOrderByHash(hash).getClientId()), false);
        return ResponseEntity.ok(resultBonusesAmount);
    }
}
