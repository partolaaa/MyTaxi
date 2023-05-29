package mytaxi.krutyporokh.controller;

import mytaxi.krutyporokh.models.UserDriverCarForm;
import mytaxi.krutyporokh.services.UserDriverCarFormService;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.services.ClientService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final CustomUserService customUserService;

    private final UserDriverCarFormService formService;
    private final DriverService driverService;
    private final ClientService clientService;

    @Autowired
    public AdminsController(CustomUserService customUserService, UserDriverCarFormService formService, DriverService driverService, ClientService clientService) {
        this.customUserService = customUserService;
        this.formService = formService;
        this.driverService = driverService;
        this.clientService = clientService;
    }

    @GetMapping("/edit-driver-form/{id}")
    public String editDriverForm(@PathVariable("id") long id,  Model model){
        CustomUser userFromSession = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", userFromSession);
        model.addAttribute("userDriverCarForm", formService.fillForm(id));
        return "admin/editDriverForm";
    }

    @PostMapping("/edit-driver")
    public String editDriver(@ModelAttribute("userDriverCarForm") UserDriverCarForm userDriverCarForm){
        driverService.updateDriver(userDriverCarForm);
        return "redirect:/admin";
    }

    @GetMapping("/edit-client-form/{id}")
    public String editClientForm(@PathVariable("id") long id,  Model model){
        CustomUser userFromSession = customUserService.getCurrentUserFromSession().get();
        model.addAttribute("user", userFromSession);
        model.addAttribute("customUser", customUserService.findUserById(id));
        return "admin/editDriverForm";
    }

    @PostMapping("/edit-driver")
    public String editClient(@ModelAttribute("customUser") CustomUser user){
        customUserService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("id") long id){
        customUserService.deleteUser(id);
        return "redirect:/admin";
    }
    @GetMapping("/create-new-driver-form")
    public String createNewDriverForm(Model model){
        model.addAttribute("user", customUserService.getCurrentUserFromSession().get());
        model.addAttribute("userDriverCarForm", new UserDriverCarForm());
        return "admin/newDriverForm";
    }
    @PostMapping("/create-new-driver")
    public String createNewDriver(@ModelAttribute("userDriverCarForm") UserDriverCarForm userDriverCarForm
                               ){
        driverService.createDriver(userDriverCarForm);
        return "redirect:/admin";
    }


    @PostMapping("/ban-user")
    public String banUser(@RequestParam("id") long id){
        customUserService.toggleUserBan(id);
        return "redirect:/admin";
    }

    @GetMapping("")
    public String showAdminPage(Model model){
        model.addAttribute("user", customUserService.getCurrentUserFromSession().get());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("drivers", driverService.getAllDrivers());

        return "admin/admin";
    }
}