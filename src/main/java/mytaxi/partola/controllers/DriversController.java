package mytaxi.partola.controllers;

import mytaxi.partola.models.Order;
import mytaxi.partola.models.OrderStatus;
import mytaxi.partola.services.OrderManagementService;
import mytaxi.partola.services.OrderSortingService;
import mytaxi.partola.services.OrderStatusService;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.ClientService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
@Controller
@RequestMapping("/driver")
public class DriversController {

    private final CustomUserService customUserService;
    private final OrderManagementService orderManagementService;
    private final OrderSortingService orderSortingService;
    private final OrderStatusService orderStatusService;
    private final DriverService driverService;
    private final ClientService clientService;

    @Autowired
    public DriversController(CustomUserService customUserService, OrderManagementService orderManagementService, OrderSortingService orderSortingService, OrderStatusService orderStatusService, DriverService driverService, ClientService clientService) {
        this.customUserService = customUserService;
        this.orderManagementService = orderManagementService;
        this.orderSortingService = orderSortingService;
        this.orderStatusService = orderStatusService;
        this.driverService = driverService;
        this.clientService = clientService;
    }
    @GetMapping("/orders")
    public String driverPage (Model model) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", customUser);

        Driver driver = driverService.findDriverByUser(customUser);
        model.addAttribute("driver", driver);
        List<Order> orders = orderManagementService.findAllOrdersForDriver(driver);
        orderSortingService.sortOrdersForDrivers(orders);
        model.addAttribute("orders", orders);

        if (driver.isBusy()){
            model.addAttribute("errorMessage", "You are already busy with an active order.");
            model.addAttribute("activeOrder", orderManagementService.findActiveOrderByDriverId(driver.getDriverId()));
        }

        return "driver/myOrders";
    }

    @GetMapping("orders/{hash}")
    public String acceptOrder (@PathVariable String hash,
                               @RequestParam(required = false) Boolean accept,
                               Model model) {
        Order order = orderManagementService.findOrderByHash(hash);

        if (Boolean.TRUE.equals(accept)) {
            orderStatusService.acceptOrder(order, model);
        }

        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", customUser);

        if (model.containsAttribute("errorMessage")) {
            Driver driver = driverService.findDriverByUser(customUser);
            model.addAttribute("driver", driver);

            List<Order> orders = orderManagementService.findAllOrdersForDriver(driver);
            orderSortingService.sortOrdersForDrivers(orders);

            model.addAttribute("orders", orders);
            return "redirect:/driver/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("client", clientService.findClientById(order.getClientId()));

        model.addAttribute("passengerName", orderManagementService.getPassengerName(order));
        model.addAttribute("passengerPhoneNumber", orderManagementService.getPassengerPhoneNumber(order));
        return "driver/activeOrder";
    }

    @PostMapping("orders/{hash}/updateStatus")
    public String updateOrderStatus(@PathVariable String hash,
                                         @RequestParam(required = false) boolean continueFlag,
                                         @ModelAttribute("clientId") long clientId,
                                         @ModelAttribute("userId") long userId,
                                         @ModelAttribute("passengerName") String passengerName,
                                         Model model) {
        Order order = orderManagementService.findOrderByHash(hash);
        if (!continueFlag) {
            // After IN_PROCESS goes COMPLETED so we stop here
            if (order.getOrderStatus() == OrderStatus.IN_PROCESS) {
                orderStatusService.updateStatus(order);
                driverService.setBusyStatusById(order.getDriverId(), false);
                clientService.setHasActiveOrderStatus(clientService.findClientById(clientId), false);

                return "redirect:/driver/orders";
            }

            orderStatusService.updateStatus(order);
        } else {
            model.addAttribute("passengerName", orderManagementService.getPassengerName(order));
            model.addAttribute("passengerPhoneNumber", orderManagementService.getPassengerPhoneNumber(order));
        }
        model.addAttribute("order", order);
        model.addAttribute(clientService.findClientById(clientId));
        model.addAttribute("user", customUserService.findUserById(userId));

        return "redirect:/driver/orders/" + hash;
    }

    @GetMapping("finished-orders")
    public String finishedOrders (Model model) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        Driver driver = driverService.findDriverById(customUser.getUserId());

        List<Order> orders = orderManagementService.findAllFinishedOrdersByDriverId(driver.getDriverId());

        model.addAttribute("user", customUser);
        model.addAttribute("orders", orders);
        model.addAttribute(driver);

        return "driver/finishedOrders";
    }
}
