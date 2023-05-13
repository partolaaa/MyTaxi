package mytaxi.partola.services;

import mytaxi.krutyporokh.dao.OrderDAO;
import mytaxi.krutyporokh.models.Order;
import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

/**
 * @author Ivan Partola
 * @date 11.05.2023
 */
@Service
public class ClientService {

    private final ClientDAO clientDAO;
    private final OrderDAO orderDAO;

    @Autowired
    public ClientService(ClientDAO clientDAO, OrderDAO orderDAO) {
        this.clientDAO = clientDAO;
        this.orderDAO = orderDAO;
    }

    public boolean clientExistsWithPhoneNumber(Client client) {
        return clientDAO.getClientByPhoneNumber(client.getPhoneNumber()).isPresent();
    }

    public void addBonusesByUserAndOrderPrice(CustomUser customUser, float orderPrice) {
        Optional<Client> client = clientDAO.getClientByUserId(customUser.getUserId());

        if (client.isPresent()){
            List<Order> orders = orderDAO.getAllOrdersByClientEmail(customUser.getEmail());

            float bonusPercent = (float) (Math.max(Math.min(orders.size()/10, 10), 1) / 100.0); // bonusPercent can't be more than 10%
            float bonusesAmount = orderPrice * bonusPercent + client.get().getBonusAmount();

            clientDAO.setBonusesByClientId(customUser.getUserId(), bonusesAmount);
        }
    }

    public void subtractBonuses(Client client) {
        clientDAO.setBonusesByClientId(client.getClientId(), 0);
    }
}
