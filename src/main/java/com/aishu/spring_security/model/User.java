package com.aishu.spring_security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private int id;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean enabled = false;

    private String phoneCode;
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "hibernateLazyInitializer", "handler"})
    private WizardSetup wizardSetup;
}
