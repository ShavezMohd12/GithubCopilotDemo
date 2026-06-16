package com.ProfitMint.login.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for signup response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupResponse {

    private String message;
    private Long userId;

    // Private constructor - use builder to create instances
    private SignupResponse() {}

    // Getters only (immutable object)
    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    // Static method to get builder instance
    public static SignupResponseBuilder builder() {
        return new SignupResponseBuilder();
    }

    // Manual Builder implementation
    public static class SignupResponseBuilder {
        private String message;
        private Long userId;

        public SignupResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public SignupResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public SignupResponse build() {
            SignupResponse response = new SignupResponse();
            response.message = this.message;
            response.userId = this.userId;
            return response;
        }
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "message='" + message + '\'' +
                ", userId=" + userId +
                '}';
    }
}
