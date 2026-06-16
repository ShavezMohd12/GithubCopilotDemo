package com.ProfitMint.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for user signup request.
 */
public class SignupRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private final String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private final String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private final String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private final String password;

    @Size(max = 50, message = "Referral code must not exceed 50 characters")
    private final String referral;

    public SignupRequest(String name, String email, String phone, String password, String referral) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.referral = referral;
    }

//    // Private constructor - accepts builder
//    private SignupRequest(SignupRequestBuilder builder) {
//        this.name = builder.name;
//        this.email = builder.email;
//        this.phone = builder.phone;
//        this.password = builder.password;
//        this.referral = builder.referral;
//    }

    // Getters only (immutable object)
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getReferral() {
        return referral;
    }



    // Static method to get builder instance
//    public static SignupRequestBuilder builder() {
//        return new SignupRequestBuilder();
//    }
//
//    // Manual Builder implementation
//    public static class SignupRequestBuilder {
//        private String name;
//        private String email;
//        private String phone;
//        private String password;
//        private String referral;
//
//        public SignupRequestBuilder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public SignupRequestBuilder email(String email) {
//            this.email = email;
//            return this;
//        }
//
//        public SignupRequestBuilder phone(String phone) {
//            this.phone = phone;
//            return this;
//        }
//
//        public SignupRequestBuilder password(String password) {
//            this.password = password;
//            return this;
//        }
//
//        public SignupRequestBuilder referral(String referral) {
//            this.referral = referral;
//            return this;
//        }
//
//        public SignupRequest build() {
//            return new SignupRequest(this);
//        }
//    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", referral='" + referral + '\'' +
                '}';
    }
}
