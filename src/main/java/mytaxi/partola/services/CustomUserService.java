package mytaxi.partola.services;

import mytaxi.partola.dao.CustomUserDAO;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */
@Service
public class CustomUserService {
    private final CustomUserDAO customUserDAO;

    @Autowired
    public CustomUserService(CustomUserDAO customUserDAO) {
        this.customUserDAO = customUserDAO;
    }

    public boolean exists (CustomUser customUser) {
        return customUserDAO.findUserByEmail(customUser.getEmail()).isPresent();
    }
}
