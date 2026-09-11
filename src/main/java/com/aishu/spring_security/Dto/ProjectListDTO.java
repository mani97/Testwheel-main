package com.aishu.spring_security.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProjectListDTO {

    String projectName;
    int projectId;
    int createdById;
    String createdByName;
    LocalDateTime createdAt;

}
