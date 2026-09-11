package com.aishu.spring_security.dao;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
