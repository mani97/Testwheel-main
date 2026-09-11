package com.aishu.spring_security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aishu.spring_security.Repository.UserRepo;

@Controller
public class ValidatorController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return !userRepo.existsByUsername(username);
        // true if available, false if already taken
    }

}
