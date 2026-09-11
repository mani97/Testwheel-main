package com.aishu.spring_security.controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class oauth2Controller {


    @GetMapping("/login-google")
    public String google(){
        return "dashboard";

    }
    @GetMapping("/login-microsoft")
    public String microsoft(){
        return "dashboard";

    }
}
