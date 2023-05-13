package mytaxi.partola.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 10.05.2023
 */
@Controller
public class ClientsController {

    private final OrderDAO orderDAO;
    private final ClientDAO clientDAO;
    private final CustomUserService customUserService;

    @Autowired
    public ClientsController(OrderDAO orderDAO, ClientDAO clientDAO, CustomUserService customUserService) {
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
        this.customUserService = customUserService;
    }

    @GetMapping("/my-orders")
    public String myOrders (Model model) {
        CustomUser currentUser = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", currentUser);

        Client client = clientDAO.getClientByUserId(currentUser.getUserId()).get();
        model.addAttribute("client", client);

        model.addAttribute("orders", orderDAO.getAllOrdersByClientEmail(currentUser.getEmail()));

        return "myOrders";
    }
}
