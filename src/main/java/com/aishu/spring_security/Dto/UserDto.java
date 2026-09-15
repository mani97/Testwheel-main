package com.aishu.spring_security.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "First name is required!")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true)
    @Size(max = 30, message = "Email must be less than 30 characters!!")
    private String username;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean enabled = false;

    private String phoneCode = "+91";
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{5,15}$", message = "Phone must be 5–15 digits") // germany having 5-11
    @Column(nullable = false)
    private String phone;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    private String pictureUrl;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean onboardingCompleted = false;
}
