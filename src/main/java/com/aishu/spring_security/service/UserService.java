package com.aishu.spring_security.service;

import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println("User:"+user.getPassword());
        return userRepo.save(user);
    }
    public void updatePassword(String phone, String newPassword) {
        User user = userRepo.findByPhone(phone).orElseThrow(() -> new RuntimeException("User not found"));

        // Hash the new password
        String encodedPassword = encoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setConfirmPassword(encodedPassword); // keep consistent

        userRepo.save(user);
    }

    public boolean existsByUsername(String username) {

        return userRepo.existsByUsername(username);
    }

    public boolean existsByPhone(String phone) {
        return userRepo.existsByPhone(phone);
    }


}
