package mytaxi.krutyporokh.services;

import mytaxi.krutyporokh.models.Driver;
import mytaxi.krutyporokh.security.DriverDetails;
import mytaxi.partola.dao.CustomUserDAO;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverDetailsService implements UserDetailsService {
    private final CustomUserDAO customUserDAO;
    @Autowired
    public DriverDetailsService(CustomUserDAO customUserDAO) {
        this.customUserDAO = customUserDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       Optional<CustomUser> customUser = customUserDAO.findUserByEmail(s);

       if(customUser.isEmpty()) throw new UsernameNotFoundException("Driver not found!");

       return null;
    }
}
