package mytaxi.krutyporokh.controller;

import mytaxi.partola.models.Car;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.ClientService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final CustomUserService customUserService;
    private final DriverService driverService;
    private final ClientService clientService;
    @Autowired
    public AdminsController(CustomUserService customUserService, DriverService driverService, ClientService clientService) {
        this.customUserService = customUserService;
        this.driverService = driverService;
        this.clientService = clientService;
    }

    @PostMapping("/ban-user/{user-id}")
    public String banUser(@PathVariable("user-id") Long userId){
        customUserService.banUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/unban-user/{user-id}")
    public String unbanUser(@PathVariable("user-id") Long userId){
        customUserService.unbanUser(userId);
        return "redirect:/admin-users";
    }

    @GetMapping("/delete-user/{user-id}")
    public String deleteUser(@PathVariable("user-id") Long userId) {
        customUserService.deleteUser(userId);
        return "redirect:/admin-users";
    }

    @GetMapping("/edit-client/{user-id}")
    public String showEditClientForm(@PathVariable("user-id") Long userId,
                                     Model model){
        Optional<CustomUser> client = customUserService.getUserById(userId);
        model.addAttribute("client", client);
        return "edit-client";
    }

    @PostMapping("/edit-client")
    public String editClient(@ModelAttribute("client") Client client){
        clientService.updateClient(client);
        return "redirect:/admin/clients";
    }

    @GetMapping("/edit-driver/{user-id}")
    public String showEditDriverForm(@PathVariable("user-id") Long userId,
                                     Model model){
        Optional<CustomUser> driver = customUserService.getUserById(userId);
        model.addAttribute("driver", driver);
        return "edit-driver";
    }

    @PostMapping("/edit-driver")
    public String editDriver(@ModelAttribute("driver") Driver driver,
                             @ModelAttribute("user") CustomUser user,
                             @ModelAttribute("car") Car car){
        driverService.updateDriver(driver, user, car);
        return "redirect:/admin/drivers";
    }

    @GetMapping("/create-driver")
    public String showCreateDriverForm(Model model){
        model.addAttribute("user", new CustomUser());
        model.addAttribute("driver", new Driver());
        model.addAttribute("car", new Car());
        return "create-driver";
    }

    @PostMapping("/create-driver")
    public String createDriver(@ModelAttribute("user") CustomUser user,
                               @ModelAttribute("driver") Driver driver,
                               @ModelAttribute("car") Car car){
        driverService.createDriver(user,driver,car);
        return "redirect:/admin/drivers";
    }
    @GetMapping("/clients")
    public String allClients(Model model){
        model.addAttribute("clients", clientService.getAllClients());
        return "clients";
    }

    @GetMapping("/drivers")
    public String allDrivers(Model model){
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "drivers";
    }
    @GetMapping("")
    public String showUserAccounts(){
        return "admin";
    }
}