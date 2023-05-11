package mytaxi.partola.util;

import mytaxi.partola.models.Client;
import mytaxi.partola.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Ivan Partola
 * @date 11.05.2023
 */
@Component
public class ClientValidator implements Validator {

    private final ClientService clientService;

    @Autowired
    public ClientValidator(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Client.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Client client = (Client) o;

        if (clientService.clientExistsWithPhoneNumber(client)) {
            errors.rejectValue("phoneNumber", "", "User with this phone number already exists");
        }
    }
}
