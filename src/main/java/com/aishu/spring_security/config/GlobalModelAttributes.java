package com.aishu.spring_security.config;

import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.dao.UserPrinciple;
import com.aishu.spring_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private UserRepo userRepo;

    @ModelAttribute("currentUser")
    public User addUserDetails(Model model,Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserPrinciple userPrincipal) {
                // Load full user entity from DB
                User user= userRepo.findByUsername(userPrincipal.getUsername())
                        .orElse(null);
                if (user != null) {
                    model.addAttribute("currentUser", user);

                    String firstLetter = user.getUsername() != null && !user.getUsername().isEmpty()
                            ? user.getUsername().substring(0, 1).toUpperCase()
                            : "?";
                    model.addAttribute("firstLetter", firstLetter);
                    return user;
                }
            }
        }
        return null;
    }
}
