package mytaxi.partola.services;

import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 11.05.2023
 */
@Service
public class ClientService {

    private final ClientDAO clientDAO;

    @Autowired
    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public boolean clientExistsWithPhoneNumber(Client client) {
        return clientDAO.getClientByPhoneNumber(client.getPhoneNumber()).isPresent();
    }
}
