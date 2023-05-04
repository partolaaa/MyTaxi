package mytaxi.partola.util;

import mytaxi.partola.models.CustomUser;
import mytaxi.partola.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Ivan Partola
 * @date 01.05.2023
 */
@Component
public class CustomUserValidator implements Validator {

    private final CustomUserService customUserService;

    @Autowired
    public CustomUserValidator(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomUser.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomUser customUser = (CustomUser) o;

        if (customUserService.userExistsWithEmail(customUser)) {
            errors.rejectValue("email", "", "User with this email already exists");
        }
    }
}
