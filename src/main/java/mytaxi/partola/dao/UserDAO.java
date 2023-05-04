package mytaxi.partola.dao;

import mytaxi.partola.models.Client;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(CustomUser customUser) {
        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));

        jdbcTemplate.update(
                "INSERT INTO \"User\" (user_id, name, email, password) VALUES (nextval('user_id_seq'), ?, ?, ?)",
                customUser.getName(),
                customUser.getEmail(),
                customUser.getPassword());
    }

    public Optional<CustomUser> findUserByEmail(String email) {
        return jdbcTemplate.query("select * from \"User\" where email=?",
                        new Object[]{email},
                        new BeanPropertyRowMapper<>(CustomUser.class))
                .stream().findAny();
    }
}
