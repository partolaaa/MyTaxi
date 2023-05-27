package mytaxi.partola.services;

import mytaxi.krutyporokh.models.Order;
import mytaxi.krutyporokh.services.OrderManagementService;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Ivan Partola
 * @date 11.05.2023
 */
@Service
public class ClientService {

    private final ClientDAO clientDAO;
    private final OrderManagementService orderManagementService;

    @Autowired
    public ClientService(ClientDAO clientDAO, OrderManagementService orderManagementService) {
        this.clientDAO = clientDAO;
        this.orderManagementService = orderManagementService;
    }

    public boolean clientExistsWithPhoneNumber(Client client) {
        return clientDAO.findClientByPhoneNumber(client.getPhoneNumber()).isPresent();
    }

    public void updateRating(Client client, int rating) {
        client.setNumberOfRatings(client.getNumberOfRatings() + 1);
        client.setTotalRatings(client.getTotalRatings() + rating);
        client.setRating((float)client.getTotalRatings() / client.getNumberOfRatings());

        clientDAO.updateRating(client);
    }

    public void addBonusesByUserAndOrderPrice(CustomUser customUser, float orderPrice) {
        Optional<Client> client = clientDAO.findClientById(customUser.getUserId());

        if (client.isPresent()){
            List<Order> orders = orderManagementService.findFinishedOrdersByClientId(client.get().getClientId());

            float bonusPercent = (float) (Math.max(Math.min(orders.size()/10, 10), 1) / 100.0); // bonusPercent can't be more than 10%
            float bonusesAmount = orderPrice * bonusPercent + client.get().getBonusAmount();

            clientDAO.setBonusesByClientId(customUser.getUserId(), bonusesAmount);
        }
    }

    public void subtractBonuses(long id, Order order) {
        Client client = findClientById(id);
        float bonusesAmount = client.getBonusAmount();
        float orderPrice = order.getPrice();


        if (order.isPayWithBonuses()) {
            if (orderPrice >= bonusesAmount) {
                clientDAO.setBonusesByClientId(id, 0);
            } else {
                clientDAO.setBonusesByClientId(id, orderPrice - bonusesAmount);
            }
        }
    }

    public Client findClientById(long id) {
        return clientDAO.findClientById(id).get();
    }

    public void setHasActiveOrderStatus(Client client, boolean status) {
        clientDAO.setHasActiveOrderStatus(client, status);
    }

    public void subtractSomeBonuses(long id, float currentBonusesAmount, float subtractBonusesAmount) {
        clientDAO.setBonusesByClientId(id, currentBonusesAmount - subtractBonusesAmount);
    }
}
