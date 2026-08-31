package com.zerohunger.backend.controller;

import com.zerohunger.backend.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("role", "DONOR");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String role,
                            @RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String fullName,
                            @RequestParam String email,
                            @RequestParam(required = false) String phone,
                            @RequestParam(required = false) String address,
                            @RequestParam(required = false) String businessType,
                            @RequestParam(required = false) String focusArea,
                            @RequestParam(required = false) String area,
                            Model model,
                            HttpServletRequest request) {

        if (registrationService.usernameTaken(username)) {
            model.addAttribute("error", "Username already taken. Please choose another.");
            model.addAttribute("role", role);
            return "register";
        }
        if (registrationService.emailTaken(email)) {
            model.addAttribute("error", "An account with this email already exists.");
            model.addAttribute("role", role);
            return "register";
        }

        switch (role) {
            case "DONOR" -> registrationService.registerDonor(username, password, fullName, email,
                    phone, address, businessType);
            case "NGO" -> registrationService.registerNgo(username, password, fullName, email,
                    phone, address, focusArea);
            case "VOLUNTEER" -> registrationService.registerVolunteer(username, password, fullName, email,
                    phone, area);
            default -> {
                model.addAttribute("error", "Invalid role selected.");
                model.addAttribute("role", role);
                return "register";
            }
        }

        return "redirect:/login?registered=true";
    }
}
