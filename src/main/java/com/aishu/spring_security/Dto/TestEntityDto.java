package com.aishu.spring_security.Dto;

import java.util.List;

import com.aishu.spring_security.model.ApkUpload;
import com.aishu.spring_security.model.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntityDto {

    private int id;
    private String testName;
    private String modules;
    private String tag;
    private String description;
    private String websiteUrl;
    private String apiProtocol;
    private boolean backendApiTest;
    private Project project;
    private int projectId; // expose project reference if needed
    private List<ApkUpload> apkUpload;
}