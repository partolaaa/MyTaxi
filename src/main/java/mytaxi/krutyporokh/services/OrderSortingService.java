package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.models.Order;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author Ivan Partola
 * @date 25.05.2023
 */
@Service
public class OrderSortingService {
    // Sort from newest to old
    public void sortOrdersForClients(List<Order> orders) {
        orders.sort((o1, o2) -> o2.getBookingDatetime().compareTo(o1.getBookingDatetime()));
    }

    // Sort from old to newest, because old orders must be done first
    public void sortOrdersForDrivers(List<Order> orders) {
        orders.sort(Comparator.comparing(Order::getBookingDatetime));
    }
}
