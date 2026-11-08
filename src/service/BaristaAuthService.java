package service;

import java.util.Map;

/**
 * Deliberately minimal: one in-memory username/password pair, no {@code Barista} entity, no
 * roles, no password hashing. The barista console is shared, so there is nothing to tell apart
 * once someone is past this check.
 */
public class BaristaAuthService {

    private final Map<String, String> credentials = Map.of("barista", "coffee123");

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return password.equals(credentials.get(username));
    }
}
