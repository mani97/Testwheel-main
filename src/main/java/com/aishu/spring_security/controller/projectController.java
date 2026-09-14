package com.aishu.spring_security.controller;

import com.aishu.spring_security.Dto.ProjectDto;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.dao.UserPrinciple;
import com.aishu.spring_security.model.User;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.aishu.spring_security.Repository.ProjectRepository;
import com.aishu.spring_security.model.Project;
import com.aishu.spring_security.model.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class projectController {

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/createproject")
    public String createProject1(Model model) {

        model.addAttribute("project", new ProjectDto());
        return "create-project";
    }

    @GetMapping("/createproject2")
    public String createProject2(Model model) {

        // model.addAttribute("listEmployee",employeeService.getAllEmployee());
        return "create-project-2";
    }

    @PostMapping(value = "/saveproject", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> saveProjectJson(@Valid @RequestBody ProjectDto projectDto,
            BindingResult bindingResult,
            Authentication authentication, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("message", "Validation failed");
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            response.put("errors", errors);
            return response;
        }
        User currentUser = userRepo
                .findByUsername(authentication.getName())
                .orElseThrow();

        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectUrl(projectDto.getProjectUrl());

        if (currentUser != null) {
            project.setCreatedBy(currentUser);
            project.setUsername(currentUser);
            project.setCreatedAt(LocalDateTime.now());
        }

        Project saved = projectRepo.save(project);

        response.put("success", true);
        response.put("message", "Project saved successfully!");
        response.put("project", saved);

        return response; // JSON response
    }

    @PostMapping(value = "/saveproject", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveProjectForm(@Valid @ModelAttribute("projectDto") ProjectDto projectDto,
            BindingResult bindingResult,
            @ModelAttribute("currentUser") User currentUser,
            Model model) {

        if (bindingResult.hasErrors()) {
            // redisplay the form with errors
            return "create-project"; // Thymeleaf template name
        }

        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectUrl(projectDto.getProjectUrl());

        if (currentUser != null) {
            project.setCreatedBy(currentUser);
            project.setUsername(currentUser);
            project.setCreatedAt(LocalDateTime.now());
        }

        projectRepo.save(project);

        // add success message for Thymeleaf
        model.addAttribute("successMessage", "Project saved successfully!");
        return "redirect:/testlist";
    }

    @GetMapping("/projects")
    @ResponseBody
    @Transactional
    public List<ProjectDto> getProjects(Authentication authentication) {
        /*
         * Get currently authenticated user
         */
        User currentUser = userRepo
                .findByUsername(authentication.getName())
                .orElseThrow();
        List<Project> projects = projectRepo.findByCreatedBy(currentUser);
        return projects.stream()
                .map(p -> new ProjectDto(
                        p.getProjectId(),
                        p.getProjectName(),
                        p.getCreatedBy() != null ? Long.valueOf(p.getCreatedBy().getId()) : null,
                        p.getCreatedBy() != null ? p.getCreatedBy().getFirstName() : "",
                        p.getProjectUrl(),
                        p.getUsername() != null ? Long.valueOf(p.getUsername().getId()) : null,
                        p.getUsername() != null ? p.getUsername().getFirstName() : "new",
                        p.getCreatedAt()))
                .collect(java.util.stream.Collectors.toList());
    }

    // @GetMapping("/project-cards")
    // @ResponseBody
    // @Transactional
    // public List<com.aishu.spring_security.Dto.ProjectCardDTO>
    // getProjectCards(Authentication authentication) {
    // User currentUser = userRepo
    // .findByUsername(authentication.getName())
    // .orElseThrow();
    // List<Project> projects = projectRepo.findByCreatedBy(currentUser);

    // return projects.stream().map(p -> new
    // com.aishu.spring_security.Dto.ProjectCardDTO(
    // p.getProjectId(),

    // p.getProjectName(),
    // p.getTests() != null ? p.getTests().size() : 0,
    // p.getUsername() != null ? 1 : 0,
    // p.getCreatedBy() != null ? p.getCreatedBy().getFirstName() : "",
    // p.getCreatedAt())).collect(java.util.stream.Collectors.toList());
    // }
}
