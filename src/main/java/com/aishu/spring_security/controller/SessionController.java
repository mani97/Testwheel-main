package com.aishu.spring_security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

    @PostMapping("/projects/select")
    public String selectProject(
            @RequestParam("projectId") int projectId,
            @RequestParam("projectName") String projectName,
            HttpSession session) {

        session.setAttribute("SelectedProjectId", projectId);
        session.setAttribute("SelectedProjectName", projectName);

        return "success";
    }

    @GetMapping("/projects/selected")
    public Map<String, Object> getSelectedProject(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Integer projectId = (Integer) session.getAttribute("SelectedProjectId");
        String projectName = (String) session.getAttribute("SelectedProjectName");

        response.put("selectedProjectId", projectId);
        response.put("selectedProjectName", projectName);

        return response;
    }

}
