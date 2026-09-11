package com.aishu.spring_security.service;


import com.aishu.spring_security.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
public class EmailcheckService {
    @Autowired
    private UserRepo userRepo;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // GET /check-email?email=someone@gmail.com
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return false;
        }
        // Return true if email is available (not in DB), false if already exists
        return !userRepo.existsByUsername(email.trim());
    }
}
