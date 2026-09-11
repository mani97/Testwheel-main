package com.aishu.spring_security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {

    // Extract cookie by name
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    // Create HttpOnly cookie for access token
    public static Cookie createAccessTokenCookie(String token) {
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);      // set to false for local HTTP dev compatibility (change to true in HTTPS production)
        cookie.setPath("/");          // available for all endpoints
        cookie.setMaxAge(15 * 60);    // 15 minutes
        return cookie;
    }

    // Create HttpOnly cookie for refresh token
    public static Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);      // set to false for local HTTP dev compatibility
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        return cookie;
    }

    // Delete cookie by setting maxAge = 0
    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0); // expire immediately
        return cookie;
    }
}

