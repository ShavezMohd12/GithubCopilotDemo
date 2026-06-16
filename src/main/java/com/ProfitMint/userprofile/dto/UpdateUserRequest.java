package com.ProfitMint.userprofile.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO for update user profile request.
 */
public class UpdateUserRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;



    public UpdateUserRequest() {}

    public UpdateUserRequest(String name, String phone, String referral) {
        this.name = name;
        this.phone = phone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

