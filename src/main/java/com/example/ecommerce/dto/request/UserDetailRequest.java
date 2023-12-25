package com.example.ecommerce.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class UserDetailRequest {
    private String firstname;
    private String lastname;
    private Date date_of_birth;
    private String bio;
    private String avatar;
    private String address;
    private String phone;
    private String gender;
    private String country;
    private String city;
}
