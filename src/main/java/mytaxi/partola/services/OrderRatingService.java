package mytaxi.partola.services;

import mytaxi.partola.dao.OrderDAO;
import mytaxi.partola.models.Order;
import mytaxi.partola.models.OrderStatus;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
import mytaxi.partola.models.Role;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 25.05.2023
 */
@Service
public class OrderRatingService {

    private final CustomUserService customUserService;
    private final ClientService clientService;
    private final DriverService driverService;
    private final OrderManagementService orderManagementService;
    private final OrderDAO orderDAO;

    public OrderRatingService(CustomUserService customUserService, ClientService clientService, DriverService driverService, OrderManagementService orderManagementService, OrderDAO orderDAO) {
        this.customUserService = customUserService;
        this.clientService = clientService;
        this.driverService = driverService;
        this.orderManagementService = orderManagementService;
        this.orderDAO = orderDAO;
    }

    public void rateTrip (String hash, int rating) {
        CustomUser customUser = customUserService.getCurrentUserFromSession().get();
        Order order = orderManagementService.findOrderByHash(hash);

        // If current user is Driver -> rates Client
        if (customUser.getRole().equals(Role.ROLE_DRIVER)) {
            Client client = clientService.findClientById(order.getClientId());
            clientService.updateRating(client, rating);

            if (order.getOrderStatus().equals(OrderStatus.RATED_BY_CLIENT)) {
                order.setOrderStatus(OrderStatus.RATED_BY_ALL);
            } else if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                order.setOrderStatus(OrderStatus.RATED_BY_DRIVER);
            }
        } else if (customUser.getRole().equals(Role.ROLE_CLIENT)) {
            Driver driver = driverService.findDriverById(order.getDriverId());
            driverService.updateRating(driver, rating);
            if (order.getOrderStatus().equals(OrderStatus.RATED_BY_DRIVER)) {
                order.setOrderStatus(OrderStatus.RATED_BY_ALL);
            } else if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                order.setOrderStatus(OrderStatus.RATED_BY_CLIENT);
            }
        }

        orderDAO.setOrderStatus(hash, order.getOrderStatus());
    }
}
