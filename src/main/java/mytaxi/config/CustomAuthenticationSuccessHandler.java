package mytaxi.config;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
/**
 * @author Ivan Partola
 * @date 13.05.2023
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_DRIVER")) {
            redirectStrategy.sendRedirect(request, response, "/driver/orders");
        } else if (roles.contains("ROLE_ADMIN")) {
            redirectStrategy.sendRedirect(request, response, "/admin");
        } else {
            redirectStrategy.sendRedirect(request, response, "/order");
        }
    }
}
