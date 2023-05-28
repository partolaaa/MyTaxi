package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Ivan Partola
 * @date 25.05.2023
 */
@Service
public class OrderManagementService {
    private final OrderDAO orderDAO;
    private final CustomUserService customUserService;

    @Autowired
    public OrderManagementService(OrderDAO orderDAO, CustomUserService customUserService) {
        this.orderDAO = orderDAO;
        this.customUserService = customUserService;
    }

    public Order findOrderByHash (String hash) {
        return orderDAO.findOrderByHash(hash).get();
    }

    public void createNewOrder( Order order, Client client) {
        orderDAO.createNewOrder(order, client);
    }

    public List<Order> findAllOrdersByClientId(long id) {
        return orderDAO.findAllOrdersByClientId(id);
    }

    public List<Order> findAllOrdersForDriver(Driver driver) {
        return orderDAO.findAllOrdersForDriver(driver);
    }

    public Order findActiveOrderByDriverId(long id) {
        return orderDAO.findActiveOrderByDriverId(id).get();
    }

    public List<Order> findAllFinishedOrdersByDriverId(long id) {
        return orderDAO.findAllFinishedOrdersByDriverId(id);
    }
    public List<Order> findFinishedOrdersByClientId(long id) {
        return orderDAO.findFinishedOrdersByClientId(id);
    }

    public String getPassengerName(Order order) {
        if (order.getPassengerName().equals("") || order.getPassengerName() == null) {
            return customUserService.findUserById(order.getClientId()).getName();
        } else {
            return order.getPassengerName();
        }
    }

    public String getPassengerPhoneNumber(Order order) {
        if (order.getPassengerPhoneNumber().equals("") || order.getPassengerPhoneNumber() == null) {
            return customUserService.findUserById(order.getClientId()).getName();
        } else {
            return order.getPassengerPhoneNumber();
        }
    }
}
