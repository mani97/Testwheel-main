package com.aishu.spring_security.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class CsrfController {

    @GetMapping("/csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Map<String, String> tokenInfo = new HashMap<>();
        tokenInfo.put("headerName", csrfToken.getHeaderName());
        tokenInfo.put("parameterName", csrfToken.getParameterName());
        tokenInfo.put("token", csrfToken.getToken());
        return tokenInfo;
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateCsrf(HttpServletRequest request) {
        // If CSRF token is invalid, Spring Security will block this request automatically
        return ResponseEntity.ok("CSRF token validated successfully");
    }
}
