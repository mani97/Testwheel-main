package com.aishu.spring_security.controller;

import com.aishu.spring_security.Dto.ProjectCardDTO;
import com.aishu.spring_security.Repository.TestRepository;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.dao.UserPrinciple;
import com.aishu.spring_security.model.Project;
import com.aishu.spring_security.model.TestEntity;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.service.ProjectService;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
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
    ProjectService projectService;

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

        model.addAttribute(
                "selectedCreatedBy",
                createdBy != null ? createdBy : Collections.emptyList());

        long testCount = testRepository.count();
        long userCount = userRepository.count();
        model.addAttribute("testCount", testCount);
        model.addAttribute("userCount", userCount);

        // Build project cards only for tests that have a linked project
        List<ProjectCardDTO> projectCards = testRepository.findAll().stream()
                .filter(test -> test.getProject() != null)
                .map(test -> {
                    ProjectCardDTO dto = new ProjectCardDTO();
                    dto.setProjectName(test.getProject().getProjectName());
                    dto.setTestCases(test.getProject().getTests() != null
                            ? test.getProject().getTests().size()
                            : 0);
                    dto.setUsers(test.getProject().getUsername() != null ? 1 : 0);
                    dto.setCreatedBy(test.getProject().getCreatedBy() != null
                            ? test.getProject().getCreatedBy().getUsername()
                            : "");
                    dto.setCreatedAt(test.getProject().getCreatedAt());
                    return dto;
                }).collect(Collectors.toList());
        model.addAttribute("projectCards", projectCards);

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



}
