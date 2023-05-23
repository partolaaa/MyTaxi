package mytaxi.partola.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.krutyporokh.services.OrderService;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.CustomUserService;
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

    private final DriverDAO driverDAO;
    private final OrderDAO orderDAO;
    private final ClientDAO clientDAO;
    private final UserDAO userDAO;
    private final CustomUserService customUserService;
    private final OrderService orderService;

    @Autowired
    public DriversController(DriverDAO driverDAO, OrderDAO orderDAO, ClientDAO clientDAO, UserDAO userDAO, CustomUserService customUserService, OrderService orderService) {
        this.driverDAO = driverDAO;
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
        this.userDAO = userDAO;
        this.customUserService = customUserService;
        this.orderService = orderService;
    }
    @GetMapping("/orders")
    public String driverPage (Model model) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", customUser);

        Driver driver = driverDAO.findDriverByUser(customUser).get();
        model.addAttribute("driver", driver);
        List<Order> orders = orderDAO.findAllOrdersForDriver(driver);
        orderService.sortOrdersForDrivers(orders);
        model.addAttribute("orders", orders);

        if (driver.isBusy()){
            model.addAttribute("errorMessage", "You are already busy with an active order.");
            model.addAttribute("activeOrder", orderDAO.findActiveOrderByDriverId(driver.getDriverId()).get());
        }

        return "driver/myOrders";
    }

    @GetMapping("orders/{id}")
    public String selectedOrder (@PathVariable long id,
                                 Model model) {
        Order order = orderDAO.findOrderById(id).get();
        orderService.acceptOrder(order, model);

        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", customUser);

        if (model.containsAttribute("errorMessage")) {
            Driver driver = driverDAO.findDriverByUser(customUser).get();
            model.addAttribute("driver", driver);

            List<Order> orders = orderDAO.findAllOrdersForDriver(driver);
            orderService.sortOrdersForDrivers(orders);

            model.addAttribute("orders", orders);
            return "redirect:/driver/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("client", clientDAO.findClientById(order.getClientId()).get());


        model.addAttribute("passengerName", orderService.getPassengerName(order));
        return "driver/activeOrder";
    }
    @PostMapping("orders/{id}/updateStatus")
    public String updateOrderStatus(@PathVariable Long id,
                                         @RequestParam(required = false) boolean continueFlag,
                                         @ModelAttribute("clientId") long clientId,
                                         @ModelAttribute("userId") long userId,
                                         @ModelAttribute("passengerName") String passengerName,
                                         Model model) {
        Order order = orderDAO.findOrderById(id).get();
        if (!continueFlag) {
            // After IN_PROCESS goes COMPLETED so we stop here
            if (order.getOrderStatus() == OrderStatus.IN_PROCESS) {
                orderService.updateStatus(order);
                driverDAO.setBusyStatusById(order.getDriverId(), false);
                clientDAO.setHasActiveOrderStatus(clientDAO.findClientById(clientId).get(), false);

                return "redirect:/driver/orders";
            }


            orderService.updateStatus(order);
        }
        model.addAttribute("order", order);
        model.addAttribute(clientDAO.findClientById(clientId).get());
        model.addAttribute("user", userDAO.findUserById(userId).get());

        return "driver/activeOrder";
    }

    @GetMapping("finished-orders")
    public String finishedOrders (Model model) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        Driver driver = driverDAO.findDriverById(customUser.getUserId()).get();

        List<Order> orders = orderDAO.findAllFinishedOrdersByDriverId(driver.getDriverId());

        model.addAttribute("user", customUser);
        model.addAttribute("orders", orders);
        model.addAttribute(driver);

        return "driver/finishedOrders";
    }
}
