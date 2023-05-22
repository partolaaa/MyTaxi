package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.models.Role;
import mytaxi.partola.services.ClientService;
import mytaxi.partola.services.CustomUserService;
import mytaxi.partola.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final ClientDAO clientDAO;
    private final DriverDAO driverDAO;
    private final CustomUserService customUserService;
    private final ClientService clientService;
    private final DriverService driverService;

    @Autowired
    public OrderService(OrderDAO orderDAO, UserDAO userDAO, ClientDAO clientDAO, DriverDAO driverDAO, CustomUserService customUserService, ClientService clientService, DriverService driverService) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.clientDAO = clientDAO;
        this.driverDAO = driverDAO;
        this.customUserService = customUserService;
        this.clientService = clientService;
        this.driverService = driverService;
    }

    public void acceptOrder (Order order, Model model) {
        Driver driver = driverDAO.findDriverByUser(customUserService.getCurrentUserFromSession().get()).get();

        if ( driver.isBusy() && !Objects.equals(driver.getDriverId(), order.getDriverId())) {
            model.addAttribute("errorMessage", "You are already busy with an active order."); // here I want to send error to view that driver already has one active order
            return;
        }

        order.setOrderStatus(OrderStatus.ACCEPTED);

        driverDAO.setBusyStatusById(driver.getDriverId(), true);
        order.setDriverId(driver.getDriverId());

        orderDAO.setOrderStatus(order, OrderStatus.ACCEPTED);
        orderDAO.assignDriver(driver, order);
    }

    // Sort from newest to old
    public void sortOrdersForClients(List<Order> orders) {
        orders.sort((o1, o2) -> o2.getBookingDatetime().compareTo(o1.getBookingDatetime()));
    }

    // Sort from old to newest, because old orders must be done first
    public void sortOrdersForDrivers(List<Order> orders) {
        orders.sort(Comparator.comparing(Order::getBookingDatetime));
    }

    public String getPassengerName(Order order) {
        if (order.getPassengerName().equals("") || order.getPassengerName() == null) {
            return userDAO.findUserById(order.getClientId()).get().getName();
        } else {
            return order.getPassengerName();
        }
    }

    public void updateStatus (Order order) {
        order.setOrderStatus(order.getOrderStatus().next());
    }

    public void rateTrip (long id, int rating) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        Order order = orderDAO.findOrderById(id).get();

        // If current user is Driver -> rates Client
        if (customUser.getRole().equals(Role.ROLE_DRIVER)) {
            Client client = clientDAO.findClientById(order.getClientId()).get();
            clientService.updateRating(client, rating);

            if (order.getOrderStatus().equals(OrderStatus.RATED_BY_CLIENT)) {
                order.setOrderStatus(OrderStatus.RATED_BY_ALL);
            } else if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                order.setOrderStatus(OrderStatus.RATED_BY_DRIVER);
            }
        } else if (customUser.getRole().equals(Role.ROLE_CLIENT)) {
            Driver driver = driverDAO.findDriverById(order.getDriverId()).get();
            driverService.updateRating(driver, rating);

            if (order.getOrderStatus().equals(OrderStatus.RATED_BY_DRIVER)) {
                order.setOrderStatus(OrderStatus.RATED_BY_ALL);
            } else if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                order.setOrderStatus(OrderStatus.RATED_BY_CLIENT);
            }
        }

        orderDAO.setOrderStatus(order, order.getOrderStatus());
    }
}
