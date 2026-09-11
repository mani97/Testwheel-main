package com.aishu.spring_security.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SmsService {


    @Value("${twilio.phone-number}")
    private String fromPhone;

    public void sendOtp(String toPhone, String otp) {
        Message message = Message.creator(
                new PhoneNumber(toPhone),
                new PhoneNumber(fromPhone),
                "Your OTP code is: " + otp
        ).create();

        System.out.println("Sent OTP: " + message.getSid());
    }


    public void sendOt(String phone, String otp) {
        // Integrate with Twilio, AWS SNS, or other SMS provider
        System.out.println("Sending OTP " + otp + " to phone " + phone);
    }

}

