package com.ProfitMint.login.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for user profile response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String referral;
    private String createdAt;

    // Private constructor - use builder to create instances
    private UserProfileResponse() {}

    // Getters only (immutable object)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getReferral() {
        return referral;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Static method to get builder instance
    public static UserProfileResponseBuilder builder() {
        return new UserProfileResponseBuilder();
    }

    // Manual Builder implementation
    public static class UserProfileResponseBuilder {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String referral;
        private String createdAt;

        public UserProfileResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserProfileResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserProfileResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserProfileResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserProfileResponseBuilder referral(String referral) {
            this.referral = referral;
            return this;
        }

        public UserProfileResponseBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserProfileResponse build() {
            UserProfileResponse response = new UserProfileResponse();
            response.id = this.id;
            response.name = this.name;
            response.email = this.email;
            response.phone = this.phone;
            response.referral = this.referral;
            response.createdAt = this.createdAt;
            return response;
        }
    }

    @Override
    public String toString() {
        return "UserProfileResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", referral='" + referral + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
