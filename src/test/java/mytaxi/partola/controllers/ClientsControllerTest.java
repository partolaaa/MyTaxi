package mytaxi.partola.controllers;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.services.OrderService;
import mytaxi.partola.models.Driver;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Partola
 * @date 14.05.2023
 */
@SpringBootTest
class ClientsControllerTest {

    @Autowired
    private ClientsController clientsController;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderDAO orderDAO;

    @Test
    public void myOrdersTest () {
        Order order1 = new Order();
        order1.setBookingDatetime("19/05/2023 11:45");

        Order order2 = new Order();
        order2.setBookingDatetime("18/05/2023 11:45");

        List<Order> orders = Arrays.asList(order1, order2);

        Mockito.when(orderDAO.findAllOrdersByClientId(1)).thenReturn(orders);

        List<Order> sortedOrders = orderDAO.findAllOrdersByClientId(1);
        orderService.sortOrdersForClients(sortedOrders);

        System.out.println(sortedOrders);
        assertEquals(Arrays.asList(order1, order2), sortedOrders);
    }

    @Test
    public void driverOrdersTest () {
        Order order1 = new Order();
        order1.setBookingDatetime("18/05/2023 11:45");

        Order order2 = new Order();
        order2.setBookingDatetime("19/05/2023 11:45");

        Driver driver = new Driver();
        driver.setDriverId(2L);
        driver.setCarId(1);

        List<Order> orders = Arrays.asList(order1, order2);

        Mockito.when(orderDAO.findAllOrdersForDriver(driver)).thenReturn(orders);


        List<Order> sortedOrders = orderDAO.findAllOrdersForDriver(driver);
        orderService.sortOrdersForDrivers(sortedOrders);

        System.out.println(sortedOrders);
        assertEquals(Arrays.asList(order1, order2), sortedOrders);
    }
}