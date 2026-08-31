package com.zerohunger.backend.controller;

import com.zerohunger.backend.entity.*;
import com.zerohunger.backend.repository.DonorRepository;
import com.zerohunger.backend.service.FoodDonationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/donor")
public class DonorController {

    private final DonorRepository donorRepository;
    private final FoodDonationService donationService;

    public DonorController(DonorRepository donorRepository, FoodDonationService donationService) {
        this.donorRepository = donorRepository;
        this.donationService = donationService;
    }

    private Donor currentDonor(Authentication auth) {
        return donorRepository.findByAppUser_Username(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Donor profile not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Donor donor = currentDonor(auth);
        List<FoodDonation> donations = donationService.getDonationsForDonor(donor.getId());
        model.addAttribute("donor", donor);
        model.addAttribute("donations", donations);
        double totalGiven = donations.stream().mapToDouble(FoodDonation::getTotalQuantity).sum();
        model.addAttribute("totalGiven", totalGiven);
        return "donor/dashboard";
    }

    @GetMapping("/donate")
    public String donateForm(Model model) {
        model.addAttribute("itemCount", 3);
        return "donor/donate";
    }

    @PostMapping("/donate")
    public String submitDonation(Authentication auth,
                                  @RequestParam List<String> foodType,
                                  @RequestParam List<Double> foodQuantity,
                                  @RequestParam List<String> foodExpiry,
                                  @RequestParam String pickupAddress,
                                  @RequestParam(required = false) Double pickupLat,
                                  @RequestParam(required = false) Double pickupLon) {

        Donor donor = currentDonor(auth);

        List<FoodItem> items = new ArrayList<>();
        for (int i = 0; i < foodType.size(); i++) {
            if (foodType.get(i) != null && !foodType.get(i).isBlank() && foodQuantity.get(i) != null && foodQuantity.get(i) > 0) {
                items.add(new FoodItem(foodType.get(i), foodQuantity.get(i), foodExpiry.get(i)));
            }
        }

        LocationCoordinates location = new LocationCoordinates(pickupLat, pickupLon, null, null);
        donationService.submitDonation(donor, items, pickupAddress, location);

        return "redirect:/donor/dashboard";
    }
}
