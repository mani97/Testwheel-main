package com.aishu.spring_security.controller;

import com.aishu.spring_security.Dto.ProjectCardDTO;
import com.aishu.spring_security.Repository.ProjectRepository;
import com.aishu.spring_security.Repository.TestRepository;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.controller.Mapper.Mapper;
import com.aishu.spring_security.model.Project;

import com.aishu.spring_security.model.User;
import com.aishu.spring_security.service.ProjectService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class testListController {

        @Autowired
        TestRepository testRepository;

        @Autowired
        UserRepo userRepository;

        @Autowired
        ProjectRepository projectRepo;

        @Autowired
        ProjectService projectService;

        @Autowired
        ProjectRepository projectRepository;

        @GetMapping("/projectcard")
        public String createcard(Model model) {
                List<Project> projects = projectRepo.findAll();
                model.addAttribute("projects", projects);
                return "project-list";
        }

        @GetMapping("/project-cards")
        @ResponseBody
        @Transactional
        public List<com.aishu.spring_security.Dto.ProjectCardDTO> getProjectCards(Authentication authentication) {
                User currentUser = userRepository
                                .findByUsername(authentication.getName())
                                .orElseThrow();
                List<Project> projects = projectRepo.findByCreatedBy(currentUser);

                return projects.stream().map(p -> new com.aishu.spring_security.Dto.ProjectCardDTO(
                                p.getProjectId(),
                                p.getProjectName(),
                                p.getTests() != null ? p.getTests().size() : 0,
                                p.getUsername() != null ? 1 : 0,
                                p.getCreatedBy() != null ? p.getCreatedBy().getFirstName() : "",
                                p.getCreatedAt(),
                                p.getProjectUrl()))
                                .collect(java.util.stream.Collectors.toList());
        }

        @GetMapping("/testlist")
        public String ProjectList(@RequestParam(defaultValue = "newest") String sort,
                        @RequestParam(required = false) List<Long> createdBy,
                        Authentication authentication,
                        Model model) {

                User currentUser = userRepository.findByUsername(authentication.getName())
                                .orElseThrow();

                List<User> users = userRepository.findAll();
                model.addAttribute("currentUser", currentUser);
                model.addAttribute("users", users);
                model.addAttribute("selectedSort", sort);
                model.addAttribute("selectedCreatedBy", createdBy != null ? createdBy : Collections.emptyList());

                // Get projects created by current user
                List<Project> projects = projectRepository.findByCreatedBy(currentUser);

                // Apply "Created By" filter if selected
                if (createdBy != null && !createdBy.isEmpty()) {
                        projects = projects.stream()
                                        .filter(p -> p.getCreatedBy() != null
                                                        && createdBy.contains((long) p.getCreatedBy().getId()))
                                        .collect(Collectors.toList());
                }

                // Apply sorting
                switch (sort) {
                        case "oldest" -> projects.sort(Comparator.comparing(Project::getCreatedAt));
                        case "nameAsc" -> projects.sort(
                                        Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER));
                        case "nameDesc" -> projects.sort(Comparator
                                        .comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER).reversed());
                        default -> projects.sort(Comparator.comparing(Project::getCreatedAt).reversed()); // newest
                }

                // Convert to DTOs
                List<ProjectCardDTO> projectCardUserSpecific = projects.stream()
                                .map(Mapper::toProjectCardDto)
                                .collect(Collectors.toList());

                model.addAttribute("projectCards", projectCardUserSpecific);

                return "project-list";
        }

        @GetMapping("/testrequest/count")
        @ResponseBody
        public Map<String, Long> getTestRequestCount() {
                Map<String, Long> counts = new HashMap<>();
                counts.put("users", userRepository.count());
                counts.put("tests", testRepository.count());
                return counts;
        }

        @PostMapping("/projects/update")
        public String updateProject(@RequestParam int projectId,
                        @RequestParam String projectName,
                        @RequestParam String projectUrl) {
                System.out.println("projectId :" + projectId);
                Project project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
                project.setProjectName(projectName);
                project.setProjectUrl(projectUrl);
                projectRepository.save(project);
                return "redirect:/testlist";
        }

        @PostMapping("/projects/delete")

        @Transactional
        public String deleteProject(
                        @RequestParam int projectId,
                        Authentication authentication) {

                User currentUser = userRepository
                                .findByUsername(authentication.getName())
                                .orElseThrow();

                Project project = projectRepo.findById(projectId)
                                .orElseThrow(() -> new RuntimeException("Project not found"));

                // IMPORTANT: only allow the owner to delete their project
                if (project.getCreatedBy() == null ||
                                project.getCreatedBy().getId() != currentUser.getId()) {
                        throw new AccessDeniedException("You are not allowed to delete this project");
                }

                projectRepository.delete(project);
                return "redirect:/testlist";
        }
}
