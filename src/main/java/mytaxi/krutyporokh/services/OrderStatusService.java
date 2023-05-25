package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.ClientService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;

@Service
public class OrderStatusService {
    private final OrderDAO orderDAO;
    private final CustomUserService customUserService;
    private final OrderManagementService orderManagementService;
    private final ClientService clientService;
    private final DriverService driverService;

    @Autowired
    public OrderStatusService(OrderDAO orderDAO, CustomUserService customUserService, OrderManagementService orderManagementService, ClientService clientService, DriverService driverService) {
        this.orderDAO = orderDAO;
        this.customUserService = customUserService;
        this.orderManagementService = orderManagementService;
        this.clientService = clientService;
        this.driverService = driverService;
    }

    public String getPassengerName(Order order) {
        if (order.getPassengerName().equals("") || order.getPassengerName() == null) {
            return customUserService.findUserById(order.getClientId()).getName();
        } else {
            return order.getPassengerName();
        }
    }

    public void updateStatus (Order order) {
        order.setOrderStatus(order.getOrderStatus().next());
        orderDAO.setOrderStatus(order.getOrderId(), order.getOrderStatus());
    }

    public void cancelOrder(long id) {
        orderDAO.setOrderStatus(id, OrderStatus.CANCELLED);
    }

    public void subtractBonusesIfOrderWasCancelled(long id) {
        Order order = orderManagementService.findOrderById(id);

        // If bonuses were used, we don't subtract anything
        if (order.isPayWithBonuses()) return;

        float orderPrice = order.getPrice();
        Client client = clientService.findClientById(order.getClientId());

        List<Order> orders = orderDAO.findFinishedOrdersByClientId(order.getClientId());

        float bonusPercent = (float) (Math.max(Math.min(orders.size()/10, 10), 1) / 100.0); // bonusPercent can't be more than 10%

        float subtractBonusesAmount = orderPrice * bonusPercent;

        clientService.subtractSomeBonuses(order.getClientId(), client.getBonusAmount(), subtractBonusesAmount);
    }

    public void acceptOrder (Order order, Model model) {
        Driver driver = driverService.findDriverByUser(customUserService.getCurrentUserFromSession().get());

        if ( driver.isBusy() && !Objects.equals(driver.getDriverId(), order.getDriverId())) {
            model.addAttribute("errorMessage", "You are already busy with an active order."); // here I want to send error to view that driver already has one active order
            return;
        }

        order.setOrderStatus(OrderStatus.ACCEPTED);

        driverService.setBusyStatusById(driver.getDriverId(), true);
        order.setDriverId(driver.getDriverId());

        orderDAO.setOrderStatus(order.getOrderId(), OrderStatus.ACCEPTED);
        orderDAO.assignDriver(driver, order);
    }
}
