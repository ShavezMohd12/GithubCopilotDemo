package com.ProfitMint.login.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * Utility class for cookie creation and management.
 */
@Component
public class CookieUtil {

    @Value("${app.session.cookie-name:PROFITMINT_SESSION}")
    private String cookieName;

    @Value("${app.session.remember-me-duration-days:30}")
    private int rememberMeDurationDays;

    private static final String COOKIE_PATH = "/";

    /**
     * Create a session cookie with the given token.
     *
     * @param token      the session token
     * @param rememberMe whether to create a persistent cookie
     * @return the ResponseCookie
     */
    public ResponseCookie createSessionCookie(String token, boolean rememberMe) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(COOKIE_PATH);

        if (rememberMe) {
            builder.maxAge(Duration.ofDays(rememberMeDurationDays));
        }
        // If rememberMe is false, don't set maxAge - this creates a session cookie

        return builder.build();
    }

    /**
     * Create a cookie to clear/delete the session.
     *
     * @return the ResponseCookie with maxAge of 0
     */
    public ResponseCookie createLogoutCookie() {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path(COOKIE_PATH)
                .maxAge(0)
                .build();
    }

    /**
     * Add the session cookie to the response.
     *
     * @param response   the HTTP response
     * @param token      the session token
     * @param rememberMe whether to create a persistent cookie
     */
    public void addSessionCookie(HttpServletResponse response, String token, boolean rememberMe) {
        ResponseCookie cookie = createSessionCookie(token, rememberMe);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Clear the session cookie from the response.
     *
     * @param response the HTTP response
     */
    public void clearSessionCookie(HttpServletResponse response) {
        ResponseCookie cookie = createLogoutCookie();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Extract the session token from the request cookies.
     *
     * @param request the HTTP request
     * @return Optional containing the token if found
     */
    public Optional<String> extractSessionToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isBlank())
                .findFirst();
    }

    /**
     * Get the cookie name.
     *
     * @return the cookie name
     */
    public String getCookieName() {
        return cookieName;
    }
}
