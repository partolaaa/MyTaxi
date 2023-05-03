package mytaxi.krutyporokh.security;

import mytaxi.krutyporokh.models.Driver;
import mytaxi.partola.dao.CustomUserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;


public class DriverDetails implements UserDetails {
    private final Driver driver;
    private final CustomUserDAO customUserDAO;

    public DriverDetails(Driver driver, CustomUserDAO customUserDAO) {
        this.driver = driver;
        this.customUserDAO = customUserDAO;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return customUserDAO.getUserPasswordById(driver.getDriver_id());
    }

    @Override
    public String getUsername() {
        return customUserDAO.getUserEmailById(driver.getDriver_id());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
