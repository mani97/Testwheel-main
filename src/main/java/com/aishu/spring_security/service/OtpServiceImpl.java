package com.aishu.spring_security.service;

import com.aishu.spring_security.service.OtpService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpEntry> otpStore = new HashMap<>();
    private final Random random = new Random();

    @Override
    public String generateOtp(String phone) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpStore.put(phone, new OtpEntry(otp, LocalDateTime.now().plusMinutes(5)));
        return otp;
    }

    @Override
    public boolean isValidOtp(String phone, String otp) {
        OtpEntry entry = otpStore.get(phone);
        if (entry == null) return false;
        if (LocalDateTime.now().isAfter(entry.expiry)) return false;
        return entry.value.equals(otp);
    }

    @Override
    public void clearOtp(String phone) {
        otpStore.remove(phone);
    }

    private static class OtpEntry {
        String value;
        LocalDateTime expiry;
        OtpEntry(String value, LocalDateTime expiry) {
            this.value = value;
            this.expiry = expiry;
        }
    }

    public void sendOtp(String phone, String otp) {
        // Integrate with Twilio, AWS SNS, or other SMS provider
        System.out.println("Sending OTP " + otp + " to phone " + phone);
    }
}
