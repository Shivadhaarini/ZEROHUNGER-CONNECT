package com.zerohunger.backend.controller;

import com.zerohunger.backend.entity.FoodDonation;
import com.zerohunger.backend.entity.VolunteerProfile;
import com.zerohunger.backend.repository.VolunteerProfileRepository;
import com.zerohunger.backend.service.FoodDonationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/volunteer")
public class VolunteerController {

    private final VolunteerProfileRepository volunteerProfileRepository;
    private final FoodDonationService donationService;

    public VolunteerController(VolunteerProfileRepository volunteerProfileRepository,
                                FoodDonationService donationService) {
        this.volunteerProfileRepository = volunteerProfileRepository;
        this.donationService = donationService;
    }

    private VolunteerProfile currentVolunteer(Authentication auth) {
        return volunteerProfileRepository.findByAppUser_Username(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Volunteer profile not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        VolunteerProfile volunteer = currentVolunteer(auth);
        model.addAttribute("volunteer", volunteer);
        model.addAttribute("assignedDonations", donationService.getDonationsForVolunteer(volunteer.getId()));
        return "volunteer/dashboard";
    }

    @PostMapping("/toggle-availability")
    public String toggleAvailability(Authentication auth) {
        VolunteerProfile volunteer = currentVolunteer(auth);
        volunteer.setAvailable(!volunteer.isAvailable());
        volunteerProfileRepository.save(volunteer);
        return "redirect:/volunteer/dashboard";
    }

    @PostMapping("/confirm-delivery/{id}")
    public String confirmDelivery(@PathVariable Long id, @RequestParam(required = false) String remarks) {
        FoodDonation donation = donationService.getById(id);
        donationService.confirmDelivery(donation, remarks == null ? "" : remarks);
        return "redirect:/volunteer/dashboard";
    }
}
