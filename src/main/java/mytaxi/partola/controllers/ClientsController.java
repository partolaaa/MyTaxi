package mytaxi.partola.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.services.OrderService;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Ivan Partola
 * @date 10.05.2023
 */
@Controller
public class ClientsController {

    private final OrderDAO orderDAO;
    private final ClientDAO clientDAO;
    private final CustomUserService customUserService;
    private final OrderService orderService;

    @Autowired
    public ClientsController(OrderDAO orderDAO, ClientDAO clientDAO, CustomUserService customUserService, OrderService orderService) {
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
        this.customUserService = customUserService;
        this.orderService = orderService;
    }

    @GetMapping("/my-orders")
    public String myOrders (Model model) {
        CustomUser currentUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", currentUser);
        Client client = clientDAO.findClientById(currentUser.getUserId()).get();
        model.addAttribute("client", client);

        List<Order> orders = orderDAO.findAllOrdersByClientId(client.getClientId());
        orderService.sortOrdersForClients(orders);
        model.addAttribute("orders", orders);

        return "client/myOrders";
    }
}
