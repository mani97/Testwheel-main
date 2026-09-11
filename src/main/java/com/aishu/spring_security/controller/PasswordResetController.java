package com.aishu.spring_security.controller;

import com.aishu.spring_security.dao.ResetPasswordRequest;
import com.aishu.spring_security.service.OtpService;
import com.aishu.spring_security.service.SmsService;
import com.aishu.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private SmsService smsService;

    // Step 1: Forgot Password Form
    @GetMapping("/forgot-password-mail")
    public String showForgotForm(Model model) {
        model.addAttribute("phone", "");
        return "forgot-password-phone";
    }

    @PostMapping("/forgot-password-phone")
    public String processForgot(@RequestParam("phone") String phone,
            RedirectAttributes redirectAttributes) {
        if (!userService.existsByPhone(phone)) {
            redirectAttributes.addFlashAttribute("signinError", true);
            redirectAttributes.addFlashAttribute("message", "Phone not registered!");
            return "redirect:/forgot-password-phone";
        }
        String otp = otpService.generateOtp(phone);
        smsService.sendOtp(phone, otp);

        redirectAttributes.addFlashAttribute("signinSuccess", true);
        redirectAttributes.addFlashAttribute("message", "OTP sent to your phone.");
        return "redirect:/verify-otp?phone=" + phone;
    }

    // Step 2: Verify OTP
    @GetMapping("/verify-otp")
    public String showOtpForm(@RequestParam("phone") String phone, Model model) {
        model.addAttribute("phone", phone);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("phone") String phone,
            @RequestParam("otp") String otp,
            RedirectAttributes redirectAttributes) {
        if (otpService.isValidOtp(phone, otp)) {
            return "redirect:/reset-password?phone=" + phone;
        } else {
            redirectAttributes.addFlashAttribute("signinError", true);
            redirectAttributes.addFlashAttribute("message", "Invalid or expired OTP!");
            return "redirect:/verify-otp?phone=" + phone;
        }
    }

    // Step 3: Reset Password
    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("phone") String phone, Model model) {
        ResetPasswordRequest dto = new ResetPasswordRequest();
        dto.setPhone(phone);
        model.addAttribute("reset", dto);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("reset") ResetPasswordRequest reset,
            RedirectAttributes redirectAttributes) {
        if (!reset.getNewPassword().equals(reset.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("signinError", true);
            redirectAttributes.addFlashAttribute("message", "Passwords do not match!");
            return "redirect:/reset-password?phone=" + reset.getPhone();
        }
        userService.updatePassword(reset.getPhone(), reset.getNewPassword());
        otpService.clearOtp(reset.getPhone());

        redirectAttributes.addFlashAttribute("signinSuccess", true);
        redirectAttributes.addFlashAttribute("message", "Password reset successful!");
        return "redirect:/login";
    }
}
