package com.aishu.spring_security.controller;

import com.aishu.spring_security.Repository.TokenRepository;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.model.VerificationToken;
import com.aishu.spring_security.service.CookieUtil;
import com.aishu.spring_security.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class tokenController {

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/verify")
    @Transactional
    public String verifyAccount(@RequestParam("token") String tokenValue,
            RedirectAttributes redirectAttributes) {

        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(tokenValue);
        System.out.println("token received: " + tokenValue);

        if (optionalToken.isPresent()) {
            VerificationToken vToken = optionalToken.get();

            if (!vToken.isExpired()) {
                User user = vToken.getUser();
                user.setEnabled(true);
                userRepo.save(user);
                tokenRepository.delete(vToken);
                System.out.println("token matching, account activated for user: " + user.getUsername());

                redirectAttributes.addFlashAttribute("message",
                        user.getUsername() + " - Your account has been activated successfully! Please Sign In.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("message", "Token expired.");
                return "redirect:/signup";
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid token.");
            return "redirect:/signup";
        }
    }

}
