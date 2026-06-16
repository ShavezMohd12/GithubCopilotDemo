package com.ProfitMint.login.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for login response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private String message;
    private Long userId;
    private String name;
    private String email;

    // Private constructor - use builder to create instances
    private LoginResponse() {}

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Static method to get builder instance
    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    // Manual Builder implementation
    public static class LoginResponseBuilder {
        private String message;
        private Long userId;
        private String name;
        private String email;

        public LoginResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public LoginResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public LoginResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LoginResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public LoginResponse build() {
            LoginResponse response = new LoginResponse();
            response.message = this.message;
            response.userId = this.userId;
            response.name = this.name;
            response.email = this.email;

            return response;
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
