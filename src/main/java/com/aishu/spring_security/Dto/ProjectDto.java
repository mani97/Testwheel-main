package com.aishu.spring_security.Dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private int projectId;

    @NotBlank(message = "Project name is required!")
    @Size(max = 25, message = "Project name must be under 25 characters!")
    private String projectName;

    // Instead of full User entity, expose only ID or name
    private Long createdBy;
    private String createdByName;

    @NotBlank(message = "Project URL is required!")
    @Size(max = 200, message = "Project URL must be under 200 characters!")
    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/.*)?$", message = "Enter a valid project URL (e.g. app.testwheel.com!)")
    private String projectUrl;

    private Long usernameId;
    private String usernameName;

    private LocalDateTime createdAt;
}
