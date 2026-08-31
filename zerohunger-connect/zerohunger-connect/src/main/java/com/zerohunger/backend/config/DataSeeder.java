package com.zerohunger.backend.config;

import com.zerohunger.backend.entity.AppUser;
import com.zerohunger.backend.entity.Role;
import com.zerohunger.backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Runs once on application startup. Creates the default Admin account
 * (there's no public registration flow for Admin - by design, admins are
 * provisioned, not self-registered) so there's always a way into the
 * /admin dashboard on a fresh database.
 *
 * Credentials come from application.properties (zerohunger.admin.*) so
 * they can be changed per environment without touching code.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${zerohunger.admin.username}")
    private String adminUsername;

    @Value("${zerohunger.admin.password}")
    private String adminPassword;

    @Value("${zerohunger.admin.email}")
    private String adminEmail;

    public DataSeeder(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!appUserRepository.existsByUsername(adminUsername)) {
            AppUser admin = new AppUser(
                    adminUsername,
                    passwordEncoder.encode(adminPassword),
                    "System Administrator",
                    adminEmail,
                    Role.ADMIN
            );
            appUserRepository.save(admin);
            System.out.println("Default admin account created -> username: " + adminUsername
                    + " / password: " + adminPassword + " (change this after first login)");
        }
    }
}
