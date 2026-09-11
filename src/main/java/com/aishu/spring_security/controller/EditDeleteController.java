//package com.aishu.spring_security.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.aishu.spring_security.Dto.ProjectEditDto;
//import com.aishu.spring_security.Repository.ProjectRepository;
//import com.aishu.spring_security.Repository.UserRepo;
//import com.aishu.spring_security.model.Project;
//import com.aishu.spring_security.model.User;
//
//@Controller
//public class EditDeleteController {
//
//    @Autowired
//    private ProjectRepository projectRepo;
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @PostMapping("/projects/update")
//    @ResponseBody
//    public String updateProject(@RequestBody ProjectEditDto dto) {
//        Project project = projectRepo.findById(dto.getProjectId())
//                .orElseThrow(() -> new RuntimeException("Not found"));
//
//        project.setProjectName(dto.getProjectName());
//        project.setProjectUrl(dto.getProjectUrl());
//        project.setCreatedAt(dto.getCreatedAt());
//
//        // If you want to update relations:
//        User createdBy = userRepo.findById(dto.getCreatedById()).orElse(null);
//        project.setCreatedBy(createdBy);
//
//        User username = userRepo.findById(dto.getUsernameId()).orElse(null);
//        project.setUsername(username);
//
//        Project saved = projectRepo.save(project);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("project", saved);
//        return "projest-list";
//    }
//
//}
