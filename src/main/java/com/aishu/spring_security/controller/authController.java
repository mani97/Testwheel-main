package com.aishu.spring_security.controller;

import com.aishu.spring_security.Dto.UserDto;
import com.aishu.spring_security.dao.UserPrinciple;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.service.CookieUtil;
import com.aishu.spring_security.service.JwtService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class authController {

    @Autowired
    JwtService jwtService;

    // ---- Login ----
    @GetMapping("/login")
    public String login(@RequestParam(value = "logout", required = false) String logout, Model model,
            HttpSession session) {
        String loginError = (String) session.getAttribute("loginError");
        if (loginError != null) {
            model.addAttribute("loginError", loginError);
            session.removeAttribute("loginError");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

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
        model.addAttribute("user", new UserDto()); // must match th:object="${user}"

        return "tw-login";
    }

    // @GetMapping("/LoginErr")
    // public String LoginErr(Model model) {
    // model.addAttribute("user", new UserDto());
    // return "testwheel-404-error-page";
    // }

    // ---- Register ----
    @GetMapping({ "/signup", "/" })
    public String register(Model model) {
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
        model.addAttribute("user", new UserDto());// must match th:object="${user}"
        return "tw-signup-new";
    }

    @GetMapping("/dashboard")
    public String profile(HttpServletRequest request, Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserPrinciple userDetails) {
                String username = userDetails.getUsername();

                model.addAttribute("email", userDetails.getUsername()); // safer if available

                // Safe first letter
                String firstLetter = (username != null && !username.isEmpty())
                        ? username.substring(0, 1)
                        : "?";

                model.addAttribute("firstLetter", firstLetter);

                return "dashboard";
            }

            if (principal instanceof DefaultOAuth2User oauthUser) {
                // OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                // Map<String, Object> oauthDetails = new HashMap<>();

                String oauthId = oauthUser.getAttribute("sub");
                String name = oauthUser.getAttribute("name");
                String email = oauthUser.getAttribute("email");
                String picture = oauthUser.getAttribute("picture");

                model.addAttribute("name", name != null ? name : "Unknown User");
                model.addAttribute("email", email != null ? email : "No Email");
                model.addAttribute("picture", picture);

                return "dashboard";
            }
        }

        return "redirect:/login"; // fallback if not authenticated
    }

    // ---- Logout (redirect) ----
    // @PostMapping("/logout")
    // public String logout(HttpServletRequest request, HttpServletResponse
    // response, Model model) {

    // // Invalidate server session
    // if (request.getSession(false) != null) {
    // request.getSession(false).invalidate();
    // }
    // Cookie refreshCookie = CookieUtil.getCookie(request, "refreshToken");
    // if (refreshCookie != null) {
    // String refreshToken = refreshCookie.getValue();
    // try {
    // String username = jwtService.extractUsername(refreshToken);
    // if (username != null) {
    // // jwtStoreService.revokeAllTokensForUser(username);
    // }
    // } catch (Exception e) {
    // // ignore if token invalid
    // }
    // }
    // // Clear cookies
    // response.addCookie(CookieUtil.deleteCookie("accessToken"));
    // response.addCookie(CookieUtil.deleteCookie("refreshToken"));
    // response.addCookie(CookieUtil.deleteCookie("jwt"));
    // response.addCookie(CookieUtil.deleteCookie("XSRF-TOKEN"));
    // response.addCookie(CookieUtil.deleteCookie("JSESSIONID"));
    // response.addCookie(CookieUtil.deleteCookie("oauth"));

    // // Show logout page
    // model.addAttribute("message", "You have been logged out successfully.");
    // return "redirect:/login?logout";
    // }
}
