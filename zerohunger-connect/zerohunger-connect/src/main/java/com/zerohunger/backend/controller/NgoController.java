package com.zerohunger.backend.controller;

import com.zerohunger.backend.entity.FoodDonation;
import com.zerohunger.backend.entity.NgoOrg;
import com.zerohunger.backend.repository.NgoOrgRepository;
import com.zerohunger.backend.repository.VolunteerProfileRepository;
import com.zerohunger.backend.service.FoodDonationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ngo")
public class NgoController {

    private final NgoOrgRepository ngoOrgRepository;
    private final VolunteerProfileRepository volunteerProfileRepository;
    private final FoodDonationService donationService;

    public NgoController(NgoOrgRepository ngoOrgRepository,
                          VolunteerProfileRepository volunteerProfileRepository,
                          FoodDonationService donationService) {
        this.ngoOrgRepository = ngoOrgRepository;
        this.volunteerProfileRepository = volunteerProfileRepository;
        this.donationService = donationService;
    }

    private NgoOrg currentNgo(Authentication auth) {
        return ngoOrgRepository.findByAppUser_Username(auth.getName())
                .orElseThrow(() -> new IllegalStateException("NGO profile not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        NgoOrg ngo = currentNgo(auth);
        model.addAttribute("ngo", ngo);
        model.addAttribute("availableDonations", donationService.getPendingUnassignedDonations());
        model.addAttribute("myDonations", donationService.getDonationsForNgo(ngo.getId()));
        model.addAttribute("volunteers", volunteerProfileRepository.findByAvailable(true));
        return "ngo/dashboard";
    }

    @PostMapping("/accept/{id}")
    public String accept(@PathVariable Long id, Authentication auth) {
        NgoOrg ngo = currentNgo(auth);
        if (!ngo.isVerified()) {
            // Unverified NGOs cannot accept donations - admin must verify first
            return "redirect:/ngo/dashboard?error=notverified";
        }
        FoodDonation donation = donationService.getById(id);
        donationService.acceptDonation(donation, ngo);
        return "redirect:/ngo/dashboard";
    }

    @PostMapping("/decline/{id}")
    public String decline(@PathVariable Long id) {
        FoodDonation donation = donationService.getById(id);
        donationService.declineDonation(donation);
        return "redirect:/ngo/dashboard";
    }

    @PostMapping("/assign-volunteer/{id}")
    public String assignVolunteer(@PathVariable Long id, @RequestParam Long volunteerId) {
        FoodDonation donation = donationService.getById(id);
        var volunteer = volunteerProfileRepository.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found"));
        donationService.assignVolunteer(donation, volunteer);
        return "redirect:/ngo/dashboard";
    }
}
