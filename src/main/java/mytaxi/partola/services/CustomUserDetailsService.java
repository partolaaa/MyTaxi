package mytaxi.partola.services;

import mytaxi.partola.dao.CustomUserDAO;
import mytaxi.partola.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserDAO customUserDAO;

    @Autowired
    public CustomUserDetailsService(CustomUserDAO customUserDAO) {
        this.customUserDAO = customUserDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new CustomUserDetails(customUserDAO.findUserByEmail(s).get());
    }
}
