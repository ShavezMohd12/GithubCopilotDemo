package com.ProfitMint.login.service;

import com.ProfitMint.login.dto.*;
import com.ProfitMint.login.entity.User;
import com.ProfitMint.login.entity.UserSession;
import com.ProfitMint.login.exception.EmailAlreadyExistsException;
import com.ProfitMint.login.exception.InvalidCredentialsException;
import com.ProfitMint.login.exception.InvalidSessionException;
import com.ProfitMint.login.exception.PhoneAlreadyExistsException;
import com.ProfitMint.login.repository.UserRepository;
import com.ProfitMint.login.repository.UserSessionRepository;
import com.ProfitMint.login.util.PasswordUtil;
import com.ProfitMint.login.util.SessionTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service class for authentication operations.
 */
@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordUtil passwordUtil;
    private final SessionTokenGenerator sessionTokenGenerator;

    @Value("${app.session.remember-me-duration-days:30}")
    private int rememberMeDurationDays;

    private static final int SESSION_DURATION_HOURS = 24;

    @Autowired
    public AuthService(UserRepository userRepository,
                       UserSessionRepository userSessionRepository,
                       PasswordUtil passwordUtil,
                       SessionTokenGenerator sessionTokenGenerator) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.passwordUtil = passwordUtil;
        this.sessionTokenGenerator = sessionTokenGenerator;
    }

    /**
     * Register a new user.
     */
    public SignupResponse signup(SignupRequest request) {
        log.info("Processing signup request for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        if (userRepository.existsByPhone(request.getPhone().toLowerCase().trim())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(request.getEmail().toLowerCase().trim())
                .phone(request.getPhone() != null ? request.getPhone().trim() : null)
                .password(passwordUtil.hashPassword(request.getPassword()))
                .referral(request.getReferral() != null ? request.getReferral().trim() : null)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return SignupResponse.builder()
                .message("User created successfully")
                .userId(savedUser.getId())
                .build();
    }

    /**
     * Authenticate a user and create a session.
     */
    public String login(LoginRequest request, HttpServletRequest servletRequest) {
        log.info("Processing login request for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for email: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        String token = sessionTokenGenerator.generateToken();

        LocalDateTime expiresAt;
        if (request.isRememberMe()) {
            expiresAt = LocalDateTime.now().plusDays(rememberMeDurationDays);
        } else {
            expiresAt = LocalDateTime.now().plusHours(SESSION_DURATION_HOURS);
        }

        UserSession session = UserSession.builder()
                .token(token)
                .user(user)
                .expiresAt(expiresAt)
                .rememberMe(request.isRememberMe())
                .ipAddress(getClientIpAddress(servletRequest))
                .userAgent(servletRequest.getHeader("User-Agent"))
                .build();

        userSessionRepository.save(session);
        log.info("Session created for user ID: {}", user.getId());

        return token;
    }

    /**
     * Get login response for a user.
     */
    @Transactional(readOnly = true)
    public LoginResponse getLoginResponse(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(InvalidCredentialsException::new);
    System.out.println("User found: " + user.getEmail());
        return LoginResponse.builder()
                .message("Login successful")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Validate a session token and return the associated user.
     */
    @Transactional(readOnly = true)
    public User validateSession(String token) {
        UserSession session = userSessionRepository.findByToken(token)
                .orElseThrow(InvalidSessionException::new);

        if (session.isExpired()) {
            log.warn("Expired session token used");
            throw new InvalidSessionException("Session has expired");
        }

        return session.getUser();
    }

    /**
     * Get user profile by session token.
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String token) {
        User user = validateSession(token);

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .referral(user.getReferral())
                .createdAt(user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    /**
     * Logout a user by invalidating their session.
     */
    public void logout(String token) {
        userSessionRepository.deleteByToken(token);
        log.info("Session invalidated");
    }

    /**
     * Logout all sessions for a user.
     */
    public void logoutAll(String token) {
        User user = validateSession(token);
        userSessionRepository.deleteAllByUserId(user.getId());
        log.info("All sessions invalidated for user ID: {}", user.getId());
    }

    /**
     * Clean up expired sessions (runs every hour).
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredSessions() {
        log.info("Cleaning up expired sessions");
        userSessionRepository.deleteExpiredSessions(LocalDateTime.now());
    }

    /**
     * Get the client IP address from the request.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
