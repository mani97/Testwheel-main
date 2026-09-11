//package com.aishu.spring_security.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class StaticResourceConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // serve /assets/** from src/main/resources/static.assets/
//        registry.addResourceHandler("/assets/**")
//                .addResourceLocations("classpath:/static.assets/");
//
//        // or serve root files like /favicon.png from an external folder
//        registry.addResourceHandler("/favicon.png")
//                .addResourceLocations("file:/Users/manikandan/projects/myapp/static.assets/favicon.png");
//
////        registry.addResourceHandler("/css/**")
////                .addResourceLocations("classpath:/static.assets/css/");
////        registry.addResourceHandler("/js/**")
////                .addResourceLocations("classpath:/static.assets/js/");
//
//    }
//}


//package com.aishu.spring_security.config;
//
//import com.aishu.spring_security.Repository.UserRepo;
//import com.aishu.spring_security.Repository.WizardRepository;
//import com.aishu.spring_security.model.User;
//import com.aishu.spring_security.service.EmailController;
//import com.aishu.spring_security.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    UserRepo userRepo;
//    @Autowired
//    private EmailController emailController;
//
//
//    @Autowired
//    WizardRepository wizardRepository;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    SessionRegistry sessionRegistry;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler successHandler() {
//        return (request, response, authentication) -> {
//            String username = authentication.getName();
//            User user = userRepo.findByUsername(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            if (!user.isOnboardingCompleted()) {
//                response.sendRedirect("/layout");
//            } else {
//                response.sendRedirect("/dashboard");
//            }
//        };
//    }
//
//    @Bean
//    public AuthenticationProvider authProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
//            @Override
//            protected void additionalAuthenticationChecks(
//                    org.springframework.security.core.userdetails.UserDetails userDetails,
//                    org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication) {
//                User user = userRepo.findByUsername(userDetails.getUsername())
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//
//                if (!user.isEnabled()) {
//                    throw new DisabledException("Account not activated");
//                }
//                super.additionalAuthenticationChecks(userDetails, authentication);
//            }
//        };
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/favicon.png", "/welcome", "/testwheel", "/login", "/signup", "/verify-otp", "/verify",
//                                "/assets/**", "/css/**", "/js/**", "/createtest2", "/forgot-password-phone",
//                                "/images/**", "/fontawesome/**", "/fonts/**", "/timeout", "/createproject2",
//                                "/", "/perform_login", "/saveWizard", "/createtest-2").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/perform_login")
//                        .successHandler(successHandler())
//                        .failureHandler((request, response, exception) -> {
//                            if (exception instanceof DisabledException) {
//                                // Disabled account → redirect to custom page
//                                String username = request.getParameter("username");
//                                User user = userRepo.findByUsername(username).orElse(null);
//
//                                String emailMessage = (user != null)
//                                        ? "Your account is not activated. Please check your email '" + user.getUsername() + "' and verify."
//                                        : "Your account is not activated. Please check your email or sign up.";
//
//                                request.getSession().setAttribute("loginError", emailMessage);
//                                // ✅ Send email notification
//                                if (user != null && user.getUsername() != null) {
//                                    try {
//                                        emailController.generateAndSendToken(user);
//                                    } catch (Exception e) {
//                                        // log error but don’t break flow
//                                        System.err.println("Failed to send activation email: " + e.getMessage());
//                                    }
//                                }
//                                response.sendRedirect("/createproject2");
//
//                            } else if (exception.getMessage() != null && exception.getMessage().contains("User not found")) {
//                                // No user found → show message on login page
//                                request.getSession().setAttribute("loginError", "No user found with the given username.");
//                                response.sendRedirect("/login");
//
//                            } else if (exception instanceof org.springframework.security.authentication.BadCredentialsException) {
//                                // Invalid credentials → show message on login page
//                                request.getSession().setAttribute("loginError", "Invalid username or password.");
//                                response.sendRedirect("/login");
//
//                            } else {
//                                // Fallback for any other errors
//                                request.getSession().setAttribute("loginError", "Authentication failed. Please try again.");
//                                response.sendRedirect("/login");
//                            }
//                        })
//
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/dashboard", true))
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout=true")
//                        .deleteCookies("JSESSIONID", "accessToken")
//                        .invalidateHttpSession(true)
//                        .permitAll()
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.sendRedirect("/login?logout=true");
//                        }))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .invalidSessionUrl("/timeout")
//                        .maximumSessions(1)
//                        .sessionRegistry(sessionRegistry)
//                        .expiredUrl("/timeout"))
//                .csrf(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}
