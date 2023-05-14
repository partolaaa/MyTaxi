package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.models.OrderStatus;
import mytaxi.partola.dao.DriverDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.Driver;
import mytaxi.partola.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final DriverDAO driverDAO;
    private final CustomUserService customUserService;

    @Autowired
    public OrderService(OrderDAO orderDAO, UserDAO userDAO, DriverDAO driverDAO, CustomUserService customUserService) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.driverDAO = driverDAO;
        this.customUserService = customUserService;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        orders.sort((o1, o2) -> {
            LocalDateTime dt1 = LocalDateTime.parse(o1.getBookingDatetime(), formatter);
            LocalDateTime dt2 = LocalDateTime.parse(o2.getBookingDatetime(), formatter);
            return dt2.compareTo(dt1);
        });
    }

    // Sort from old to newest, because old orders must be done first
    public void sortOrdersForDrivers(List<Order> orders) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        orders.sort((o1, o2) -> {
            LocalDateTime dt1 = LocalDateTime.parse(o1.getBookingDatetime(), formatter);
            LocalDateTime dt2 = LocalDateTime.parse(o2.getBookingDatetime(), formatter);
            return dt1.compareTo(dt2);
        });
    }

    public String getPassengerName(Order order) {
        if (order.getPassengerName().equals("") || order.getPassengerName() == null) {
            return userDAO.findUserById(order.getClientId()).get().getName();
        } else {
            return order.getPassengerName();
        }
    }
}
