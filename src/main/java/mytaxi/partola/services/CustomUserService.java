package mytaxi.partola.services;

import mytaxi.partola.dao.ClientDAO;
import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<CustomUser> getCurrentUserFromSession () {
        String currentUserEmail = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userDAO.findUserByEmail(currentUserEmail);
    }


    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    public CustomUser findUserById (long id) {
        return userDAO.findUserById(id).get();
    }
    public void createUser (CustomUser customUser) {
        userDAO.createUser(customUser);
    }
}
