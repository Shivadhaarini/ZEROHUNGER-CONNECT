package com.zerohunger.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Landing page + role-based redirect after login.
 * Spring Security sends every successful login to /dashboard; from there
 * we bounce the user to their role's actual dashboard URL.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboardRouter(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_DONOR")) return "redirect:/donor/dashboard";
            if (role.equals("ROLE_NGO")) return "redirect:/ngo/dashboard";
            if (role.equals("ROLE_VOLUNTEER")) return "redirect:/volunteer/dashboard";
            if (role.equals("ROLE_ADMIN")) return "redirect:/admin/dashboard";
        }
        return "redirect:/login";
    }
}
