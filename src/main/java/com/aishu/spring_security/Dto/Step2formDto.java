package com.aishu.spring_security.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step2formDto {

    @Size(max = 250, message = "Test case name must be less than 100 characters!!")
    private String testName;

    @Size(max = 250, message = "Modules must be less than 100 characters!!")
    private String modules;

    @Size(max = 250, message = "Tag must be less than 100 characters!!")
    private String tag;

    @Size(max = 250, message = "Description must be less than 250 characters!!")
    private String description;

}
