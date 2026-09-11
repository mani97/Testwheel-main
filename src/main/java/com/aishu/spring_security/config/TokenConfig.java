//package com.aishu.spring_security.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.token.DefaultTokenService;
//import org.springframework.security.core.token.SecureRandomFactoryBean;
//import org.springframework.security.core.token.TokenService;
//
//@Configuration
//public class TokenConfig {
//
//    @Bean
//    public TokenService tokenService() throws Exception {
//        DefaultTokenService tokenService = new DefaultTokenService();
//        tokenService.setServerSecret("mySecretKey"); // use a strong secret
//        tokenService.setServerInteger(123456);       // unique integer
//        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
//        return tokenService;
//    }
//}
