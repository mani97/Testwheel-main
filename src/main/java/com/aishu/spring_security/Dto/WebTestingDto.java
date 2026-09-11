package com.aishu.spring_security.Dto;

import com.aishu.spring_security.model.Project;
import com.aishu.spring_security.model.TestEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebTestingDto {

    private String testName;

    private String modules;
    private String tag;

    private String description;

    @NotBlank(message = "Website URL is required(backend)")
    @Pattern(regexp = "^https?://([\\w-]+\\.)+[\\w-]{2,}(/.*)?$", message = "Enter a valid website URL (e.g. http//app.testwheel.com)")
    @Size(max = 250, message = "Website URL must be less than 250 characters")
    private String websiteUrl;

    private boolean backendApiTest;
    private int projectId;
    private Project project;

}
