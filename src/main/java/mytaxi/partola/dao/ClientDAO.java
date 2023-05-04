package mytaxi.partola.dao;

import mytaxi.partola.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ivan Partola
 * @date 04.05.2023
 */
@Component
public class ClientDAO {
    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ClientDAO(JdbcTemplate jdbcTemplate, UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createClient(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        userDAO.createUser(client);

        int userId = jdbcTemplate.queryForObject("SELECT currval('user_id_seq')", Integer.class);

        jdbcTemplate.update(
                "INSERT INTO \"Client\" (client_id, phone_number) VALUES (?, ?)",
                userId,
                client.getPhoneNumber());
    }
}
