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
    public Map<String, Object> saveProjectJson(@RequestBody Project project,
            @ModelAttribute("currentUser") User currentUser, HttpSession session) {
        if (currentUser != null) {
            project.setCreatedBy(currentUser);
            project.setUsername(currentUser);
            project.setCreatedAt(LocalDateTime.now());
            // session.setAttribute("SelectedProjectId", project.getProjectId());// when
            // coming into card itself store
            // session
            // session.setAttribute("SelectedProjectName", project.getProjectName());//
            // information on first creation
        }

        Project saved = projectRepo.save(project);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Project saved successfully!");
        response.put("project", saved);

        return response; // JSON response
    }

    // @PostMapping(value = "/saveproject", consumes =
    // MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    // @ResponseBody
    // public Map<String, Object> saveProjectForm(@ModelAttribute Project project,
    // @ModelAttribute("currentUser") User currentUser) {
    // if (currentUser != null) {
    // project.setCreatedBy(currentUser);
    // project.setUsername(currentUser);
    // project.setCreatedAt(LocalDateTime.now());
    // }

    // Project saved = projectRepo.save(project);

    // Map<String, Object> response = new HashMap<>();
    // response.put("success", true);
    // response.put("message", "Project saved successfully!");
    // response.put("project", saved);

    // return response;
    // }

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
