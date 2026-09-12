package com.aishu.spring_security.service;

import com.aishu.spring_security.Repository.NotificationRepository;
import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.Notification;
import com.aishu.spring_security.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepo userRepo;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addSignupNotification(String username) {
        Notification note = new Notification();
        note.setTitle("New User Signup");
        note.setMessage("User " + username + " signed up successfully");
        note.setTimeAgo(LocalDateTime.now()); // later you can calculate dynamically
        note.setUserid(getCurrentUser());
        note.setRead(false);
        notificationRepository.save(note);
    }

    public List<Notification> getLatest() {
        User currentUser = getCurrentUser();
        return notificationRepository.findRecentByUserid(currentUser, PageRequest.of(0, 5));
    }

    public List<Notification> getAll() {
        User currentUser = getCurrentUser();
        return notificationRepository.findAllByUseridOrderByTimeAgoDesc(currentUser);
    }

    public void addLoginNotification(String username) {
        Notification note = new Notification();
        note.setTitle("New User Login");
        note.setMessage("User " + username + " logged in successfully");
        note.setTimeAgo(LocalDateTime.now()); // later you can calculate dynamically
        note.setUserid(getCurrentUser());
        note.setRead(false);
        notificationRepository.save(note);
    }

    public void addTestCreatedNotification(String username) {
        Notification note = new Notification();
        note.setTitle("New Testcase Created");
        note.setMessage("User " + username + " created a new testcase successfully");
        note.setTimeAgo(LocalDateTime.now()); // later you can calculate dynamically
        note.setUserid(getCurrentUser());
        note.setRead(false);
        notificationRepository.save(note);
    }

    public void markAllAsRead(User user) {
        List<Notification> notes = notificationRepository.findByUserid(user);
        notes.forEach(note -> note.setRead(true));
        notificationRepository.saveAll(notes);
    }

}
