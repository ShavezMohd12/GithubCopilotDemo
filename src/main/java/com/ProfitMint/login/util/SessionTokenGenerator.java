package com.ProfitMint.login.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating secure session tokens.
 */
@Component
public class SessionTokenGenerator {

    private static final int TOKEN_LENGTH = 32; // 256 bits
    private final SecureRandom secureRandom;

    public SessionTokenGenerator() {
        this.secureRandom = new SecureRandom();
    }

    /**
     * Generate a cryptographically secure random session token.
     *
     * @return a Base64 URL-safe encoded token
     */
    public String generateToken() {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}

