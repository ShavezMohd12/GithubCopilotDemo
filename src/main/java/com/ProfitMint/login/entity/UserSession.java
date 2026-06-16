package com.ProfitMint.login.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions", indexes = {
    @Index(name = "idx_session_token", columnList = "token", unique = true),
    @Index(name = "idx_session_user", columnList = "user_id")
})

public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "remember_me", nullable = false)
    private Boolean rememberMe;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    // Manual builder implementation
    public static class UserSessionBuilder {
        private Long id;
        private String token;
        private User user;
        private LocalDateTime expiresAt;
        private Boolean rememberMe;
        private LocalDateTime createdAt;
        private String ipAddress;
        private String userAgent;

        public UserSessionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserSessionBuilder token(String token) {
            this.token = token;
            return this;
        }

        public UserSessionBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserSessionBuilder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public UserSessionBuilder rememberMe(Boolean rememberMe) {
            this.rememberMe = rememberMe;
            return this;
        }

        public UserSessionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserSessionBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public UserSessionBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public UserSession build() {
            UserSession session = new UserSession();
            session.setId(id);
            session.setToken(token);
            session.setUser(user);
            session.setExpiresAt(expiresAt);
            session.setRememberMe(rememberMe);
            session.setCreatedAt(createdAt);
            session.setIpAddress(ipAddress);
            session.setUserAgent(userAgent);
            return session;
        }
    }

    public static UserSessionBuilder builder() {
        return new UserSessionBuilder();
    }
}