package com.aishu.spring_security.Dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCardDTO {
    private String projectName;
    private int testCases;
    private int users;
    private String createdBy;
    private LocalDateTime createdAt;
}
