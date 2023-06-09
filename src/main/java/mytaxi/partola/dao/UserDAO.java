package mytaxi.partola.dao;

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
                "INSERT INTO \"user\" (user_id, name, email, password) VALUES (nextval('user_id_seq'), ?, ?, ?)",
                customUser.getName(),
                customUser.getEmail(),
                customUser.getPassword());
    }

    public Optional<CustomUser> findUserByEmail(String email) {
        return jdbcTemplate.query("select * from \"user\" where email=?",
                        new Object[]{email},
                        new BeanPropertyRowMapper<>(CustomUser.class))
                .stream().findAny();
    }

    public Optional<CustomUser> findUserById(long id) {
        return jdbcTemplate.query("select * from \"user\" where user_id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(CustomUser.class))
                .stream().findAny();
    }

    public void banUser(Long userId) {
        jdbcTemplate.update("update \"user\" set banned=true where id=?", userId);
    }

    public void unbanUser(Long userId) {
        jdbcTemplate.update("update \"user\" set banned=false where id=?", userId);
    }

    public void deleteUser(Long userId) {
        jdbcTemplate.update("delete from \"user\" where user_id=?", userId);
    }

    public void updateUser(CustomUser user) {
        jdbcTemplate.update(
                "UPDATE \"user\" SET name = ?, email = ?, password = ?, banned = ? WHERE user_id = ?",
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.isBanned(),
                user.getUserId()
        );
    }

    public void toggleUserBan(long id, boolean banStatus) {
        jdbcTemplate.update(
                "UPDATE \"user\" SET banned = ? WHERE user_id = ?",
                banStatus,
                id
        );
    }
}
