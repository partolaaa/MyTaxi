package mytaxi.partola.services;

import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */
@Service
public class CustomUserService {
    private final UserDAO userDAO;

    @Autowired
    public CustomUserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean userExistsWithEmail(CustomUser customUser) {
        return userDAO.findUserByEmail(customUser.getEmail()).isPresent();
    }
    public boolean userExistsWithPhoneNumber(CustomUser customUser) {
        // TODO:
        return false;
    }
}
