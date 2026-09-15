package com.aishu.spring_security.controller;

import com.aishu.spring_security.Dto.UserDto;
import com.aishu.spring_security.Repository.TokenRepository;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.controller.Mapper.Mapper;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.model.VerificationToken;
import com.aishu.spring_security.service.*;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    // @Autowired
    // private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    JavaMailSender javaMailSender;

    private void populateCountries(Model model) {
        List<Map<String, Object>> countries = new ArrayList<>();
        try {
            java.net.URLConnection conn = new URL(
                    "https://raw.githubusercontent.com/samayo/country-json/master/src/country-by-calling-code.json")
                    .openConnection();
            conn.setConnectTimeout(1500);
            conn.setReadTimeout(1500);
            ObjectMapper mapper = new ObjectMapper();
            countries = mapper.readValue(conn.getInputStream(), new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            Map<String, Object> c1 = new HashMap<>();
            c1.put("country", "India");
            c1.put("calling_code", "91");
            countries.add(c1);
            Map<String, Object> c2 = new HashMap<>();
            c2.put("country", "United States");
            c2.put("calling_code", "1");
            countries.add(c2);
            Map<String, Object> c3 = new HashMap<>();
            c3.put("country", "United Kingdom");
            c3.put("calling_code", "44");
            countries.add(c3);
        }
        model.addAttribute("countries", countries);
    }

    @PostMapping("/signup")
    public String register(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Password confirmation check
            if (userDto.getPassword() != null && userDto.getConfirmPassword() != null &&
                    !userDto.getPassword().equals(userDto.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "error.user", "Passwords do not match!");
            }

            // Duplicate username (email) check
            if (userDto.getUsername() != null && !userDto.getUsername().trim().isEmpty() &&
                    userRepo.existsByUsername(userDto.getUsername().trim())) {
                result.rejectValue("username", "error.user", "Email address already exists!");
            }

            // If there are validation errors, re-render form with inline errors
            if (result.hasErrors()) {
                populateCountries(model);
                return "tw-signup-new";
            }

            // Encode password before saving
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userDto.setEnabled(false);

            User user = Mapper.userDtoToEntity(userDto);
            userRepo.save(user);

            // Generate token + send email
            String token = UUID.randomUUID().toString();
            tokenRepository.save(new VerificationToken(token, user));
            String activationLink = "http://localhost:9098/verify?token=" + token;
            try {
                emailService.sendEmail(userDto.getUsername(),
                        "Account Activation",
                        "Click the link to activate your account: " + activationLink);
                System.out.println(activationLink);
            } catch (Exception ex) {
                logger.error("Failed to send activation email to " + userDto.getUsername(), ex);
                System.out.println("Failed to send mail to id");
            }

            // Success → redirect to login with flash attributes
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message",
                    "User " + userDto.getUsername()
                            + " created successfully! Please check your email to activate your account.");
            return "redirect:/login";

        } catch (DataIntegrityViolationException e) {
            populateCountries(model);
            model.addAttribute("message", "Duplicate data detected. Please check your details.");
            return "tw-signup-new";
        } catch (Exception e) {
            e.printStackTrace();
            populateCountries(model);
            model.addAttribute("message", "Unable to complete registration. Please try again.");
            return "tw-signup-new";
        }
    }
}