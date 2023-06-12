package mytaxi.partola.services;

import mytaxi.partola.dao.OrderDAO;
import mytaxi.partola.models.Order;
import mytaxi.partola.models.OrderStatus;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public OrderStatusService(OrderDAO orderDAO, CustomUserService customUserService, OrderManagementService orderManagementService, ClientService clientService, DriverService driverService, SimpMessagingTemplate messagingTemplate) {
        this.orderDAO = orderDAO;
        this.customUserService = customUserService;
        this.orderManagementService = orderManagementService;
        this.clientService = clientService;
        this.driverService = driverService;
        this.messagingTemplate = messagingTemplate;
    }

    public String getPassengerName(Order order) {
        if (order.getPassengerName().equals("")) {
            return customUserService.findUserById(order.getClientId()).getName();
        } else {
            return order.getPassengerName();
        }
    }

    public void updateStatus (Order order) {
        order.setOrderStatus(order.getOrderStatus().next());
        orderDAO.setOrderStatus(order.getHash(), order.getOrderStatus());

        messagingTemplate.convertAndSend("/topic/orderStatus/" + order.getHash(), order);
    }

    public void cancelOrder(String hash) {
        orderDAO.setOrderStatus(hash, OrderStatus.CANCELLED);
    }

    public float subtractBonusesIfOrderWasCancelled(String hash) {
        Order order = orderManagementService.findOrderByHash(hash);

        float orderPrice = order.getPrice();
        Client client = clientService.findClientById(order.getClientId());

        List<Order> orders = orderDAO.findFinishedOrdersByClientId(order.getClientId());

        float bonusPercent = (float) (Math.max(Math.min(orders.size()/10, 10), 1) / 100.0); // bonusPercent can't be more than 10%

        float subtractBonusesAmount = orderPrice * bonusPercent;

        clientService.subtractSomeBonuses(order.getClientId(), client.getBonusAmount(), subtractBonusesAmount);

        return client.getBonusAmount() - subtractBonusesAmount;
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

        orderDAO.setOrderStatus(order.getHash(), OrderStatus.ACCEPTED);
        orderDAO.assignDriver(driver, order);
    }
}
