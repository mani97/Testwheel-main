package com.aishu.spring_security.Dto;

import com.aishu.spring_security.model.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileTestingDto {
    private String testName;
    private String modules;
    private String tag;
    private String description;
    private String apiProtocol;
    private int projectId;

}
