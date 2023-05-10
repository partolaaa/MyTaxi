package mytaxi.partola.models;

import lombok.Data;

/**
 * @author Ivan Partola
 * @date 10.05.2023
 */

public enum Role {
    ROLE_CLIENT("ROLE_CLIENT"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_DRIVER("ROLE_DRIVER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }
}
