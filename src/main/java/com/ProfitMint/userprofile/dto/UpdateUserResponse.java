package com.ProfitMint.userprofile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for update user response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserResponse {

    private String message;

    private String name;

    private String phone;

    private UpdateUserResponse() {}

    public String getMessage() {
        return message;
    }


    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }


    public static UpdateUserResponseBuilder builder() {
        return new UpdateUserResponseBuilder();
    }

    public static class UpdateUserResponseBuilder {
        private String message;
        private String name;
        private String phone;

        public UpdateUserResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public UpdateUserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }


        public UpdateUserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }


        public UpdateUserResponse build() {
            UpdateUserResponse response = new UpdateUserResponse();
            response.message = this.message;

            response.name = this.name;

            response.phone = this.phone;

            return response;
        }
    }

    @Override
    public String toString() {
        return "UpdateUserResponse{" +
                "message='" + message + '\'' +

                ", name='" + name + '\'' +

                ", phone='" + phone + '\'' +

                '}';
    }
}

