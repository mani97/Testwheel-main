package com.aishu.spring_security.controller;

import com.aishu.spring_security.model.TestEntity;
import com.aishu.spring_security.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Controller
public class dashboardController {


    @GetMapping("/welcome-loading")
    public String allTest(Model model) {

        // model.addAttribute("listEmployee",employeeService.getAllEmployee());
        return "welcome-loading";
    }

    @GetMapping("/welcome")
    public String TestWheel(Model model) {

        // model.addAttribute("listEmployee",employeeService.getAllEmployee());
        return "welcome";
    }

    @GetMapping({"vid","/layout"})
    public String layout(Model model) {

        // model.addAttribute("listEmployee",employeeService.getAllEmployee());
        return "layout";
    }





    @GetMapping("/err")
    public String Testerr(Model model) {

        // model.addAttribute("listEmployee",employeeService.getAllEmployee());
        return "testwheel-404-error-page";
    }

    @GetMapping("/timeout")
    public String Timeout(Model model) {

        // model.addAttribute("listEmployee",employeeService.getAllEmployee());
        return "testwheel-session-timed-out";
    }
}