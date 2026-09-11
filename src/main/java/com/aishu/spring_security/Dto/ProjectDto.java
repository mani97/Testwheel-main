package com.aishu.spring_security.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private int projectId;

    private String projectName;

    // Instead of full User entity, expose only ID or name
    private Long createdById;
    private String createdByName;

    private String projectUrl;

    private Long usernameId;
    private String usernameName;

    private LocalDateTime createdAt;
}
