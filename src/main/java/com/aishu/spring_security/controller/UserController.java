package com.aishu.spring_security.controller;

import com.aishu.spring_security.Repository.TokenRepository;
import com.aishu.spring_security.Repository.UserRepo;
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

    @PostMapping("/signup")
    public String register(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            // Server-side validation
            if (user.getFirstName() == null ||
                    user.getFirstName().trim().isEmpty()) {

                model.addAttribute("message", "First name is required.");
                return "tw-signup-new";
            }

            if (user.getUsername() == null ||
                    !user.getUsername().trim()
                            .matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

                model.addAttribute("message", "Please enter a valid email address.");
                return "tw-signup-new";
            }

            if (user.getPassword() == null ||
                    user.getPassword().length() < 6) {

                model.addAttribute(
                        "message",
                        "Password must be at least 6 characters long.");

                return "tw-signup-new";
            }

            if (user.getConfirmPassword() != null &&
                    !user.getConfirmPassword().isEmpty() &&
                    !user.getPassword().equals(user.getConfirmPassword())) {

                model.addAttribute("message", "Passwords do not match!");
                return "tw-signup-new";
            }

            if (userRepo.existsByUsername(user.getUsername().trim())) {

                model.addAttribute(
                        "message",
                        "Email address already exists!");

                return "tw-signup-new";
            }

            // if (user.getPhone() != null &&
            // !user.getPhone().trim().isEmpty() &&
            // userRepo.existsByPhone(user.getPhone().trim())) {

            // model.addAttribute(
            // "message",
            // "Phone number already exists!");

            // return "tw-signup-new";
            // }

            // Bean validation errors
            if (result.hasErrors()) {

                return "tw-signup-new";
            }

            // Encode password before saving
            user.setPassword(
                    passwordEncoder.encode(user.getPassword()));

            user.setEnabled(false);

            // Continue with your existing save / activation logic here
            userRepo.save(user);

            // Generate token + send email
            String token = UUID.randomUUID().toString();
            tokenRepository.save(new VerificationToken(token, user));
            String activationLink = "http://localhost:9098/verify?token=" + token;
            try {
                emailService.sendEmail(user.getUsername(),
                        "Account Activation",
                        "Click the link to activate your account: " + activationLink);
                System.out.println(activationLink);
            } catch (Exception ex) {
                logger.error("Failed to send activation email to " + user.getUsername(), ex);
                System.out.println("Failes to send mail to id");
            }

            // Success → redirect to login with flash attributes
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message",
                    "User " + user.getUsername()
                            + " created successfully! Please check your email to activate your account.");
            return "redirect:/login";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("message", "Duplicate data detected. Please check your details.");
            return "signup";
        }

        catch (Exception e) {

            e.printStackTrace();

            model.addAttribute(
                    "message",
                    "Unable to complete registration. Please try again.");

            return "tw-signup-new";
        }
    }
}