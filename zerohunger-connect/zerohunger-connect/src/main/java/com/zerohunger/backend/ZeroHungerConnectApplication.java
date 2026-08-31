package com.zerohunger.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ZeroHunger Connect - Centralized Food Donation Management System
 * Aligned with SDG 2: Zero Hunger
 *
 * Web application entry point. Bootstraps the embedded Tomcat server,
 * Spring MVC, Spring Data JPA, and Spring Security.
 */
@SpringBootApplication
public class ZeroHungerConnectApplication {
    public static void main(String[] args) {
        System.out.println("=== ZeroHunger Connect Web Application ===");
        System.out.println("SDG 2: Zero Hunger - Food Donation Management\n");
        SpringApplication.run(ZeroHungerConnectApplication.class, args);
    }
}
