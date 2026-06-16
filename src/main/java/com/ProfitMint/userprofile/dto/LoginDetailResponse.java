package com.ProfitMint.userprofile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for login detail response - returns session and user info.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDetailResponse {


    private String ipAddress;
    private String userAgent;
    private String sessionCreatedAt;

    private LoginDetailResponse() {}






    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getSessionCreatedAt() {
        return sessionCreatedAt;
    }



    public static LoginDetailResponseBuilder builder() {
        return new LoginDetailResponseBuilder();
    }

    public static class LoginDetailResponseBuilder {

        private String ipAddress;
        private String userAgent;
        private String sessionCreatedAt;


        public LoginDetailResponseBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public LoginDetailResponseBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public LoginDetailResponseBuilder sessionCreatedAt(String sessionCreatedAt) {
            this.sessionCreatedAt = sessionCreatedAt;
            return this;
        }



        public LoginDetailResponse build() {
            LoginDetailResponse response = new LoginDetailResponse();

            response.ipAddress = this.ipAddress;
            response.userAgent = this.userAgent;
            response.sessionCreatedAt = this.sessionCreatedAt;

            return response;
        }
    }

    @Override
    public String toString() {
        return "LoginDetailResponse{" +

                ", ipAddress='" + ipAddress + '\'' +
                ", sessionCreatedAt='" + sessionCreatedAt + '\'' +
                '}';
    }
}

