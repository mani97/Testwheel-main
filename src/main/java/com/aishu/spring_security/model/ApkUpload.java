package com.aishu.spring_security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "apk_upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApkUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // File details
    private String fileName;
    private String filePath;
    private long fileSize;
    private LocalDateTime uploadedAt;

    // APK metadata
    private String packageName;
    private String versionName;
    private int versionCode;

    @Column(length = 200000) // store multiple permissions as comma-separated string
    private String permissions;

    // Relationship back to TestEntity
    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestEntity testEntity;
}
