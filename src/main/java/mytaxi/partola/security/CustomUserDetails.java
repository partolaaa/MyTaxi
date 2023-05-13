package mytaxi.partola.security;

import mytaxi.partola.dao.UserDAO;
import mytaxi.partola.models.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */

public class CustomUserDetails implements UserDetails {

    private final CustomUser customUser;
    private final UserDAO userDAO;

    @Autowired
    public CustomUserDetails(CustomUser customUser, UserDAO userDAO) {
        this.customUser = customUser;
        this.userDAO = userDAO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(customUser.getRole().getValue()));
    }

    @Override
    public String getPassword() {
        return customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return customUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !customUser.isBanned();
    }
}
