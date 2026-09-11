package com.aishu.spring_security.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step2formDto {

    private String testName;

    private String modules;
    private String tag;

    @Size(max = 50, message = "Description must be less than 250 characters")
    private String description;

}
