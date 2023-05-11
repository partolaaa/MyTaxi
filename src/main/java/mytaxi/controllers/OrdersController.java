package mytaxi.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.validation.groups.OrderForAnotherPerson;
import mytaxi.krutyporokh.validation.groups.OrderForSelf;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.security.CustomUserDetails;
import mytaxi.partola.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;

@Controller
public class OrdersController {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final ClientDAO clientDAO;
    private final Validator validator;
    private final ClientService clientService;


    @Value("${googleMapsAPIKey}")
    private String googleMapsAPIKey;

    @Autowired
    public OrdersController(OrderDAO orderDAO, UserDAO userDAO, ClientDAO clientDAO, Validator validator, ClientService clientService) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.clientDAO = clientDAO;
        this.validator = validator;
        this.clientService = clientService;
    }

    @GetMapping("order")
    public String order (@ModelAttribute("order") Order order,
                         Model model) {
        model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);
        // getUsername() method returns email in our case
        String currentUserEmail = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        CustomUser currentUser = userDAO.findUserByEmail(currentUserEmail).get();
        model.addAttribute("user", currentUser);
        Client client = clientDAO.getClientByUserId(currentUser.getUserId()).get();
        model.addAttribute("bonusesAmount", client.getBonusAmount());
        return "order";
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
        //Getting email of user
        String currentUserEmail = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        //Finding a user
        CustomUser currentUser = userDAO.findUserByEmail(currentUserEmail).get();
        Client client = clientDAO.getClientByUserId(currentUser.getUserId()).get();

        //Processing validation errors
        if(bindingResult.hasErrors()){
            //Re-adding Google Maps API key to avoid problems with map crash
            model.addAttribute("googleMapsAPIKey", googleMapsAPIKey);

            model.addAttribute("user", currentUser);
            model.addAttribute("client", client);
            model.addAttribute("error", bindingResult.getAllErrors());
            return "order";
        }

        clientService.addBonusesByUserAndOrderPrice(currentUser, order.getPrice());

        orderDAO.createNewOrder(order, currentUser);

        return "redirect:/my-orders";
    }
}
