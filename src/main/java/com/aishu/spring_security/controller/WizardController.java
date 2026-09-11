package com.aishu.spring_security.controller;


import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.Repository.WizardRepository;
import com.aishu.spring_security.model.User;
import com.aishu.spring_security.model.WizardSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RestController
public class WizardController {

    @Autowired
    private WizardRepository wizardRepo;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/saveWizard")
    public WizardSetup saveWizard(@RequestBody WizardSetup wizardSetup, Model model, RedirectAttributes redirectAttributes, Principal principal) {

        // ✅ Get the logged-in user
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Update onboarding flag
        user.setOnboardingCompleted(true);
        userRepo.save(user);

        // ✅ Link wizard setup to user
        wizardSetup.setUser(user);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Onboarding completed successfully!");
        return wizardRepo.save(wizardSetup);
    }
}
