package com.aishu.spring_security.config;

import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.controller.EmailController;
import com.aishu.spring_security.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepo userRepo;
    private final EmailController emailController;

    public CustomAuthenticationFailureHandler(UserRepo userRepo, EmailController emailController) {
        this.userRepo = userRepo;
        this.emailController = emailController;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof DisabledException) {
            String username = request.getParameter("username");
            User user = userRepo.findByUsername(username).orElse(null);

            String emailMessage = (user != null)
                    ? "Your account is not activated. Please check your email '" + user.getUsername() + "' and verify."
                    : "Your account is not activated. Please check your email or sign up.";
            // Send email notification
            if (user != null && user.getUsername() != null) {
                try {
                    emailController.generateAndSendToken(user);
                } catch (Exception e) {
                    // log error but don’t break flow
                    System.err.println("Failed to send activation email: " + e.getMessage());
                }
            }

            request.getSession().setAttribute("loginError", emailMessage);
            response.sendRedirect("/createproject2");

        } else if (exception instanceof UsernameNotFoundException) {
            request.getSession().setAttribute("loginError", "No account found with the given username.");
            response.sendRedirect("/login");

        } else if (exception instanceof BadCredentialsException) {
            request.getSession().setAttribute("loginError", "Invalid username or password!.");
            response.sendRedirect("/login");

        } else {
            request.getSession().setAttribute("loginError", "Authentication failed. Please try again.");
            response.sendRedirect("/login");
        }
    }
}
