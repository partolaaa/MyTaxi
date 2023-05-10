package mytaxi.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ivan Partola
 * @date 10.05.2023
 */
@Controller
public class ClientsController {

    private final UserDAO userDAO;
    private final OrderDAO orderDAO;
    private final ClientDAO clientDAO;

    @Autowired
    public ClientsController(UserDAO userDAO, OrderDAO orderDAO, ClientDAO clientDAO) {
        this.userDAO = userDAO;
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
    }

    @GetMapping("/my-orders")
    public String myOrders (Model model) {
        String currentUserEmail = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        CustomUser currentUser = userDAO.findUserByEmail(currentUserEmail).get();
        model.addAttribute("user", currentUser);

        Client client = clientDAO.getClientByUserId(currentUser.getUserId()).get();
        model.addAttribute("client", client);

        model.addAttribute("orders", orderDAO.getAllOrdersByClientEmail(currentUserEmail));

        return "my-orders";
    }
}
