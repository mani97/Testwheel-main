package com.aishu.spring_security.service;



public interface OtpService {
    String generateOtp(String phone);
    boolean isValidOtp(String phone, String otp);
    void clearOtp(String phone);
}

