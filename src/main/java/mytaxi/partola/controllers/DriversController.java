package mytaxi.partola.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
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
    private final CustomUserService customUserService;
    private final OrderService orderService;

    @Autowired
    public DriversController(DriverDAO driverDAO, OrderDAO orderDAO, ClientDAO clientDAO, CustomUserService customUserService, OrderService orderService) {
        this.driverDAO = driverDAO;
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
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
        }

        return "driverOrders";
    }

    @GetMapping("orders/{id}")
    public String selectedOrder (@PathVariable long id, Model model) {
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
        return "selectedByDriverOrder";
    }
    @PostMapping("orders/{id}/updateStatus")
    public void updateStatus (@PathVariable long id,
                              @PathVariable String status) {

    }
}
