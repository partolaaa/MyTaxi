package mytaxi.partola.controllers;

import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.services.OrderService;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.services.ClientService;
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

    private final CustomUserService customUserService;
    private final OrderService orderService;
    private final ClientService clientService;

    @Autowired
    public ClientsController(CustomUserService customUserService, OrderService orderService, ClientService clientService) {
        this.customUserService = customUserService;
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @GetMapping("/my-orders")
    public String myOrders (Model model) {
        CustomUser currentUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", currentUser);
        Client client = clientService.findClientById(currentUser.getUserId());
        model.addAttribute("client", client);

        List<Order> orders = orderService.findAllOrdersByClientId(client.getClientId());
        orderService.sortOrdersForClients(orders);
        model.addAttribute("orders", orders);

        return "client/myOrders";
    }
}
