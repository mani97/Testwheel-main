package com.aishu.spring_security.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String username;//mail
    private String phoneCode = "+91";
    private String phone;
    private String password;
    private String confirmPassword;
    private String pictureUrl;
}
