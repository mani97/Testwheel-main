package com.aishu.spring_security.controller;

import com.aishu.spring_security.Repository.TokenRepository;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.model.VerificationToken;
import com.aishu.spring_security.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailController {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void generateAndSendToken(User user) {
        VerificationToken existingToken = tokenRepository.findByUserUsername(user.getUsername()).orElse(null);

        String token = UUID.randomUUID().toString();
        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.setExpiryDate(LocalDateTime.now().plusHours(1));
            tokenRepository.save(existingToken);
        } else {
            tokenRepository.save(new VerificationToken(token, user));
        }

        String activationLink = "http://localhost:9098/verify?token=" + token;
        emailService.sendEmail(
                user.getUsername(),
                "Account Activation",
                "Click the link to activate your account: " + activationLink);
    }

}
