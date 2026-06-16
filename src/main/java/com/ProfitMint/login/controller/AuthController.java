package com.ProfitMint.login.controller;

import com.ProfitMint.login.dto.LoginRequest;
import com.ProfitMint.login.dto.LoginResponse;
import com.ProfitMint.login.dto.SignupRequest;
import com.ProfitMint.login.dto.SignupResponse;
import com.ProfitMint.login.dto.UserProfileResponse;
import com.ProfitMint.login.exception.InvalidSessionException;
import com.ProfitMint.login.service.AuthService;
import com.ProfitMint.login.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for authentication endpoints.
 */
@RestController
@RequestMapping
@CrossOrigin(origins={"http://localhost:3000"})
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    public AuthController(AuthService authService, CookieUtil cookieUtil) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
    }

    /**
     * User signup endpt.
     *
     * @param request the signup request
     * @return the signup response
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        log.info("Received signup request for email: {}", request.getEmail());
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * User login endpoint.
     *
     * @param request         the login request
     * @param servletRequest  the HTTP servlet request
     * @param servletResponse the HTTP servlet response
     * @return the login response
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        log.info("Received login request for email: {}", request);



        // Authenticate and get session token
        String token = authService.login(request, servletRequest);

        // Set session cookie
        cookieUtil.addSessionCookie(servletResponse, token, request.isRememberMe());

        // Get login response
        LoginResponse response = authService.getLoginResponse(request.getEmail());
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * User logout endpoint.
     *
     * @param servletRequest  the HTTP servlet request
     * @param servletResponse the HTTP servlet response
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {

        log.info("Received logout request");

        // Extract session token
        String token = cookieUtil.extractSessionToken(servletRequest)
                .orElse(null);

        if (token != null) {
            authService.logout(token);
        }

        // Clear session cookie
        cookieUtil.clearSessionCookie(servletResponse);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    /**
     * Logout from all devices endpoint.
     *
     * @param servletRequest  the HTTP servlet request
     * @param servletResponse the HTTP servlet response
     * @return success message
     */
    @PostMapping("/logout-all")
    public ResponseEntity<Map<String, String>> logoutAll(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {

        log.info("Received logout-all request");

        // Extract session token
        String token = cookieUtil.extractSessionToken(servletRequest)
                .orElseThrow(InvalidSessionException::new);

        authService.logoutAll(token);

        // Clear session cookie
        cookieUtil.clearSessionCookie(servletResponse);

        return ResponseEntity.ok(Map.of("message", "Logged out from all devices successfully"));
    }

    /**
     * Get current user profile endpoint.
     *
     * @param servletRequest the HTTP servlet request
     * @return the user profile
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(HttpServletRequest servletRequest) {
        log.info("Received get current user request");

        // Extract session token
        String token = cookieUtil.extractSessionToken(servletRequest)
                .orElseThrow(InvalidSessionException::new);

        UserProfileResponse response = authService.getUserProfile(token);

        return ResponseEntity.ok(response);
    }

    /**
     * Validate session endpoint.
     *
     * @param servletRequest the HTTP servlet request
     * @return validation status
     */
    @GetMapping("/validate-session")
    public ResponseEntity<Map<String, Object>> validateSession(HttpServletRequest servletRequest) {
        log.info("Received session validation request");

        // Extract session token
        String token = cookieUtil.extractSessionToken(servletRequest)
                .orElse(null);

        if (token == null) {
            return ResponseEntity.ok(Map.of(
                    "valid", false,
                    "message", "No session found"
            ));
        }

        try {
            authService.validateSession(token);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "message", "Session is valid"
            ));
        } catch (InvalidSessionException e) {
            return ResponseEntity.ok(Map.of(
                    "valid", false,
                    "message", "Session is invalid or expired"
            ));
        }
    }
}
