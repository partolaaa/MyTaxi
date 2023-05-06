package mytaxi.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.krutyporokh.models.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class OrderController {
    private OrderDAO orderDAO;

    @Autowired
    public OrderController(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }
    @PostMapping("/createNewOrder")
    public String  createNewOrder(@Valid @ModelAttribute Order order,
                                  BindingResult bindingResult,
                                  Model model
                               ){

        if(bindingResult.hasErrors()){
            model.addAttribute("error", bindingResult.getAllErrors());
            return "order";
        }

        order.setOrderId(null); // Will be auto-incremented in DB
        order.setDriverId(null); // Current order status - not accepted by any driver, so driver - null
        order.setOrderStatus(null); // Current order status - not accepted by any driver

        orderDAO.createNewOrder(order);

        return "redirect:/client-orders";
    }
}
