package com.aishu.spring_security.controller;

import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.Notification;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class notificationController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/notifications")
    @ResponseBody
    public List<Notification> getNotifications() {

        return notificationService.getLatest();
    }

    @GetMapping("/notificationsall")
    @ResponseBody
    public List<Notification> getAllNotifications() {

        return notificationService.getAll();
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/notifications")
    public String sendMessage(String message) {
        System.out.println("message:" + message);
        return message;
    }

    @PostMapping("/notifications/mark-all-read")
    @ResponseBody
    public String markAllAsRead() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        notificationService.markAllAsRead(currentUser);
        return "success";
    }

}
