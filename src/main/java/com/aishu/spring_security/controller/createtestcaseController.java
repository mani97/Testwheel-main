package com.aishu.spring_security.controller;

import com.aishu.spring_security.Repository.TestRepository;
import com.aishu.spring_security.controller.Mapper.Mapper;
import com.aishu.spring_security.Dto.APITestingDto;
import com.aishu.spring_security.Dto.Step2formDto;
import com.aishu.spring_security.Dto.TestEntityDto;
import com.aishu.spring_security.Dto.WebTestingDto;
import com.aishu.spring_security.Repository.ApkRepo;
import com.aishu.spring_security.Repository.ProjectRepository;
import com.aishu.spring_security.model.ApkUpload;
import com.aishu.spring_security.model.Project;
import com.aishu.spring_security.model.TestEntity;
import com.aishu.spring_security.service.NotificationService;

import jakarta.validation.Valid;
import net.dongliu.apk.parser.ApkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

@Controller
public class createtestcaseController {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    ApkRepo apkRepo;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/create-testcase")
    public String createTest(@RequestParam(required = false) String step, Model model) {

        model.addAttribute("testEntity", new TestEntityDto());
        model.addAttribute("step", step != null ? step : "step1");// pass step info to Thymeleaf

        return "create-testcase";
    }

    @PostMapping("/checkform2")
    public ResponseEntity<?> createTestCase(@Valid @ModelAttribute Step2formDto step2formDto,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        // After saving, redirect to test list page
        // TestEntity entity = Mapper.toEntity(webTestingDto);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // Web Testing
    @PostMapping("/createtest1")
    public ResponseEntity<?> createWebTest(
            @Valid @ModelAttribute WebTestingDto webTestingDto,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        // Step 1: Convert TestEntityDto → WebTestingDto
        // WebTestingDto webDto = Mapper.toWebTestingDto(testEntityDto);

        if (webTestingDto.getProjectId() != 0) {
            Project project = projectRepository.findById(webTestingDto.getProjectId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Invalid project ID: " + webTestingDto.getProjectId()));
            webTestingDto.setProject(project);
        }
        // Step 3: Convert WebTestingDto → TestEntity
        TestEntity entity = Mapper.toEntity(webTestingDto);

        // testEntityDto.setWebsiteUrl(websiteUrl);
        testRepository.save(entity);

        if (entity.getProject() != null && entity.getProject().getCreatedBy() != null) {
            notificationService.addTestCreatedNotification(entity.getProject().getCreatedBy().getUsername());
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    // Mobile Testing
    @PostMapping("/createtest2")
    public ResponseEntity<?> createMobileTest(
            @Valid @ModelAttribute TestEntity testEntity,
            BindingResult result,
            @RequestParam(value = "projectId", required = false) Integer projectId,
            @RequestParam(value = "apk", required = false) MultipartFile apkFile) throws IOException {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        if (projectId != null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
            testEntity.setProject(project);
        }

        testRepository.save(testEntity);

        if (apkFile != null && !apkFile.isEmpty()) {
            Path path = Paths.get("uploads/" + apkFile.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, apkFile.getBytes());

            try (ApkParser apkParser = new ApkParser(path.toFile())) {
                ApkMeta meta = apkParser.getApkMeta();

                ApkUpload apkUpload = new ApkUpload();
                apkUpload.setFileName(apkFile.getOriginalFilename());
                apkUpload.setFilePath(path.toString());
                apkUpload.setFileSize(apkFile.getSize());
                apkUpload.setUploadedAt(LocalDateTime.now());
                apkUpload.setTestEntity(testEntity);

                apkUpload.setPackageName(meta.getPackageName());
                apkUpload.setVersionName(meta.getVersionName());
                apkUpload.setVersionCode(Math.toIntExact(meta.getVersionCode()));
                apkUpload.setPermissions(String.join(",", meta.getUsesPermissions()));

                apkRepo.save(apkUpload);
            }
        }

        if (testEntity.getProject() != null && testEntity.getProject().getCreatedBy() != null) {
            notificationService.addTestCreatedNotification(testEntity.getProject().getCreatedBy().getUsername());
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    // API Testing
    @PostMapping("/createtest3")
    public ResponseEntity<?> createApiTest(
            @Valid @ModelAttribute APITestingDto apiTestingDto,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        if (apiTestingDto.getProjectId() != 0) {
            Project project = projectRepository.findById(apiTestingDto.getProjectId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Invalid project ID: " + apiTestingDto.getProjectId()));
            apiTestingDto.setProject(project);
        }

        // Step 3: Convert apiTestingDto → TestEntity
        TestEntity entity = Mapper.toEntity(apiTestingDto);
        testRepository.save(entity);

        if (entity.getProject() != null && entity.getProject().getCreatedBy() != null) {
            notificationService.addTestCreatedNotification(entity.getProject().getCreatedBy().getUsername());
        }

        return ResponseEntity.ok(Map.of("success", true));
    }
}
