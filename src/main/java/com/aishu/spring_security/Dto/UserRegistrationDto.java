package com.aishu.spring_security.Dto;

import com.aishu.spring_security.model.VerificationToken;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationDto {

    private String firstName;

    private String lastName;

    private String username;

    private String phone;

    private String password;

    private String pictureUrl;

    private List<VerificationToken> token = new ArrayList<>();

}
