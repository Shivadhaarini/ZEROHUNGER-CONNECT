package com.zerohunger.backend.controller;

import com.zerohunger.backend.entity.DonationStatus;
import com.zerohunger.backend.entity.NgoOrg;
import com.zerohunger.backend.entity.Role;
import com.zerohunger.backend.repository.AppUserRepository;
import com.zerohunger.backend.repository.NgoOrgRepository;
import com.zerohunger.backend.service.FoodDonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.EnumMap;
import java.util.Map;

/**
 * Admin module - the web equivalent of the original console app's
 * "SYSTEM STATISTICS" + "STREAMS API DEMO" sections, plus NGO verification
 * and user management from the report's Admin module description.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FoodDonationService donationService;
    private final NgoOrgRepository ngoOrgRepository;
    private final AppUserRepository appUserRepository;

    public AdminController(FoodDonationService donationService,
                            NgoOrgRepository ngoOrgRepository,
                            AppUserRepository appUserRepository) {
        this.donationService = donationService;
        this.ngoOrgRepository = ngoOrgRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<DonationStatus, Long> counts = new EnumMap<>(DonationStatus.class);
        for (DonationStatus status : DonationStatus.values()) {
            counts.put(status, donationService.countByStatus(status));
        }

        model.addAttribute("statusCounts", counts);
        model.addAttribute("totalFoodRescued", donationService.getTotalFoodRescued());
        model.addAttribute("allDonations", donationService.getAllDonations());
        model.addAttribute("unverifiedNgos", donationService.getUnverifiedNgos());
        model.addAttribute("totalDonors", appUserRepository.findByRole(Role.DONOR).size());
        model.addAttribute("totalNgos", appUserRepository.findByRole(Role.NGO).size());
        model.addAttribute("totalVolunteers", appUserRepository.findByRole(Role.VOLUNTEER).size());
        return "admin/dashboard";
    }

    @PostMapping("/verify-ngo/{id}")
    public String verifyNgo(@PathVariable Long id) {
        NgoOrg ngo = ngoOrgRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NGO not found"));
        ngo.setVerified(true);
        ngoOrgRepository.save(ngo);
        return "redirect:/admin/dashboard";
    }
}
