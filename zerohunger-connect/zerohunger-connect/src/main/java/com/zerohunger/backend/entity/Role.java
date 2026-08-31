package com.zerohunger.backend.entity;

/**
 * User roles in the system. Drives both business routing (see
 * FoodDonationService in the original console app's pattern-matching demo)
 * and Spring Security's role-based access control.
 */
public enum Role {
    DONOR,
    NGO,
    VOLUNTEER,
    ADMIN
}
