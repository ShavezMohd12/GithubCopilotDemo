package com.ProfitMint.login.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordUtil() {
        // Strength 12 provides good security without excessive performance impact
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Hash a plain text password using BCrypt.
     *
     * @param plainPassword the plain text password
     * @return the hashed password
     */
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Verify a plain text password against a hashed password.
     *
     * @param plainPassword  the plain text password
     * @param hashedPassword the hashed password
     * @return true if the passwords match, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}

