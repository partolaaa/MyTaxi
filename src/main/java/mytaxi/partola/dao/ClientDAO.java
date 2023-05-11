package mytaxi.partola.dao;

import mytaxi.partola.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Ivan Partola
 * @date 04.05.2023
 */
@Component
public class ClientDAO {
    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;
    @Autowired
    public ClientDAO(JdbcTemplate jdbcTemplate, UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }

    @Transactional
    public void createClient(Client client) {
        userDAO.createUser(client);

        int userId = jdbcTemplate.queryForObject("SELECT currval('user_id_seq')", Integer.class);

        jdbcTemplate.update(
                "INSERT INTO \"Client\" (client_id, phone_number) VALUES (?, ?)",
                userId,
                client.getPhoneNumber());
    }

    public Optional<Client> getClientByUserId(long id) {
        return jdbcTemplate.query("select * from \"Client\" where client_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Client.class))
                .stream().findAny();
    }

    public Optional<Client> getClientByPhoneNumber(String phoneNumber) {
        return jdbcTemplate.query("select * from \"Client\" where phone_number=?",
                        new Object[]{phoneNumber},
                        new BeanPropertyRowMapper<>(Client.class))
                .stream().findAny();
    }

    public void setBonusesByClientId(long clientId, float bonusesAmount) {
        jdbcTemplate.update(
                "update \"Client\" set bonus_amount = ? where client_id = ?;",
                bonusesAmount,
                clientId);
    }
}
