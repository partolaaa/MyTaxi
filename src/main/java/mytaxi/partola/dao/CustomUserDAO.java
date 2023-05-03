package mytaxi.partola.dao;

import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class CustomUserDAO {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public CustomUserDAO(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(CustomUser customUser) {
        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));

        jdbcTemplate.update(
                "INSERT INTO \"user\" (name, password, email, role) VALUES (?, ?, ?, ?)",
                customUser.getName(),
                customUser.getPassword(),
                customUser.getEmail(),
                customUser.getPhoneNumber());
    }

    public Optional<CustomUser> findUserByEmail(String email) {
        return jdbcTemplate.query("select * from \"user\" where email=?",
                        new Object[]{email},
                        new BeanPropertyRowMapper<>(CustomUser.class))
                .stream().findAny();
    }

    public String getUserPasswordById(int user_id){
        return jdbcTemplate.queryForObject(
                "SELECT password FROM \"User\" user_id = ?",
                new Object[]{user_id}, String.class);
    }

    public String getUserEmailById(int user_id){
        return jdbcTemplate.queryForObject(
                "SELECT email FROM \"User\" user_id = ?",
                new Object[]{user_id}, String.class);
    }

}
