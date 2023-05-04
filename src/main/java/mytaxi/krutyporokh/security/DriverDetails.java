package mytaxi.krutyporokh.security;

import mytaxi.krutyporokh.dao.DriverDAO;
import mytaxi.krutyporokh.models.Driver;
import mytaxi.partola.dao.CustomUserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class DriverDetails implements UserDetails {
    private final Driver driver;
    private final DriverDAO driverDAO;

    public DriverDetails(Driver driver, DriverDAO driverDAO) {
        this.driver = driver;
        this.driverDAO = driverDAO;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return driverDAO.getDriverPasswordById(driver.getDriverId());
    }

    @Override
    public String getUsername() {
        return driverDAO.getDriverEmailById(driver.getDriverId());
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
