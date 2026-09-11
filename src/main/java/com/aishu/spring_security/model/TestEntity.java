package com.aishu.spring_security.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "testrequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Test name is required")
    @Size(max = 100, message = "Test name must be less than 100 characters")
    private String testName;

    @NotBlank(message = "Modules field is required")
    private String modules;

    private String tag;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    private String websiteUrl;

    private boolean backendApiTest;

    private String apiProtocol;

    // Relationship with ApkUpload entity
    @OneToMany(mappedBy = "testEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ApkUpload> apkUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    @JsonBackReference
    private Project project;
}
