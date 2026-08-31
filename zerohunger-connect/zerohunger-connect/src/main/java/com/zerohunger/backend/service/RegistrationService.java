package com.zerohunger.backend.service;

import com.zerohunger.backend.entity.*;
import com.zerohunger.backend.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles new-account creation for all four roles. Each registration
 * creates one AppUser (login identity) plus one role-specific profile
 * row (Donor / NgoOrg / VolunteerProfile) - mirroring the constructors
 * used for Donor/NGO/Volunteer in the original console app.
 */
@Service
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final DonorRepository donorRepository;
    private final NgoOrgRepository ngoOrgRepository;
    private final VolunteerProfileRepository volunteerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(AppUserRepository appUserRepository,
                                DonorRepository donorRepository,
                                NgoOrgRepository ngoOrgRepository,
                                VolunteerProfileRepository volunteerProfileRepository,
                                PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.donorRepository = donorRepository;
        this.ngoOrgRepository = ngoOrgRepository;
        this.volunteerProfileRepository = volunteerProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean usernameTaken(String username) { return appUserRepository.existsByUsername(username); }
    public boolean emailTaken(String email) { return appUserRepository.existsByEmail(email); }

    @Transactional
    public AppUser registerDonor(String username, String rawPassword, String fullName, String email,
                                  String phone, String address, String businessType) {
        AppUser user = createUser(username, rawPassword, fullName, email, Role.DONOR);
        Donor donor = new Donor(user, phone, address, businessType);
        donorRepository.save(donor);
        return user;
    }

    @Transactional
    public AppUser registerNgo(String username, String rawPassword, String fullName, String email,
                                String contact, String address, String focusArea) {
        AppUser user = createUser(username, rawPassword, fullName, email, Role.NGO);
        // New NGOs start unverified; an Admin must verify them (see report's verification_status field)
        NgoOrg ngo = new NgoOrg(user, contact, address, false, focusArea);
        ngoOrgRepository.save(ngo);
        return user;
    }

    @Transactional
    public AppUser registerVolunteer(String username, String rawPassword, String fullName, String email,
                                      String phone, String area) {
        AppUser user = createUser(username, rawPassword, fullName, email, Role.VOLUNTEER);
        VolunteerProfile volunteer = new VolunteerProfile(user, phone, area, true, 0);
        volunteerProfileRepository.save(volunteer);
        return user;
    }

    private AppUser createUser(String username, String rawPassword, String fullName, String email, Role role) {
        AppUser user = new AppUser(username, passwordEncoder.encode(rawPassword), fullName, email, role);
        return appUserRepository.save(user);
    }
}
