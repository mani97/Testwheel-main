package com.aishu.spring_security.config;

import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepo userRepo;
    private final UserDetailsService userDetailsService;
    private final SessionRegistry sessionRegistry;
    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(UserRepo userRepo,
            UserDetailsService userDetailsService,
            SessionRegistry sessionRegistry,
            CustomAuthenticationFailureHandler failureHandler) {
        this.userRepo = userRepo;
        this.userDetailsService = userDetailsService;
        this.sessionRegistry = sessionRegistry;
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String username = authentication.getName();
            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (!user.isOnboardingCompleted()) {
                response.sendRedirect("/welcome");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
            @Override
            protected void additionalAuthenticationChecks(
                    org.springframework.security.core.userdetails.UserDetails userDetails,
                    UsernamePasswordAuthenticationToken authentication) {

                // Step 1: Load user from DB
                User user = userRepo.findByUsername(userDetails.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                // Step 2: Run default password check
                super.additionalAuthenticationChecks(userDetails, authentication);

                // Step 3: Check enabled flag
                if (!user.isEnabled()) {
                    throw new DisabledException("Account not activated");
                }

                // Step 4: If all good, authentication succeeds
            }
        };
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/signup", "/verify", "/check-email", "/login", "/assets/**", "/css/**",
                                "/js/**",
                                "/images/**", "/favicon.png", "/favicon.ico", "/forgot-password-phone", "/verify-otp",
                                "/reset-password", "/createproject2", "/err")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler) // use custom handler
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID", "accessToken")
                        .invalidateHttpSession(true)
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/timeout")
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry)
                        .expiredUrl("/timeout"))
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/err") // 👈 page for 403 forbidden
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/err"); // 👈 page for 401 unauthorized
                        }))
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
