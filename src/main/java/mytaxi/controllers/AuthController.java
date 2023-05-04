package mytaxi.controllers;

import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.util.CustomUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */
@Controller
public class AuthController {
    private final ClientDAO clientDAO;
    private final CustomUserValidator customUserValidator;

    @Autowired
    public AuthController(ClientDAO clientDAO, CustomUserValidator customUserValidator) {
        this.clientDAO = clientDAO;
        this.customUserValidator = customUserValidator;
    }

    @GetMapping("register")
    public String register (@ModelAttribute("client") Client client) {
        return "register";
    }

    @PostMapping("register")
    public String registerNewUser(@ModelAttribute("client") @Valid Client client,
                                  BindingResult bindingResult,
                                  Model model) {

        customUserValidator.validate(client, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "register";
        }
        clientDAO.createClient(client);
        return "redirect:/login";
    }

    @GetMapping({"/","/login"})
    public String login() {
        return "login";
    }
}
