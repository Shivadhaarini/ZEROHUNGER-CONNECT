package com.zerohunger.backend.service;

import com.zerohunger.backend.entity.*;
import com.zerohunger.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FoodDonationService - the web equivalent of the original console app's
 * FoodDonationService class. Same responsibilities (submit, track, verify,
 * aggregate stats) but operating over the database instead of an in-memory
 * ArrayList, and returning data for controllers/views instead of printing
 * to System.out.
 */
@Service
public class FoodDonationService {

    private final FoodDonationRepository donationRepository;
    private final DistributionRepository distributionRepository;
    private final NgoOrgRepository ngoOrgRepository;
    private final VolunteerProfileRepository volunteerProfileRepository;

    public FoodDonationService(FoodDonationRepository donationRepository,
                                DistributionRepository distributionRepository,
                                NgoOrgRepository ngoOrgRepository,
                                VolunteerProfileRepository volunteerProfileRepository) {
        this.donationRepository = donationRepository;
        this.distributionRepository = distributionRepository;
        this.ngoOrgRepository = ngoOrgRepository;
        this.volunteerProfileRepository = volunteerProfileRepository;
    }

    @Transactional
    public FoodDonation submitDonation(Donor donor, List<FoodItem> items, String pickupAddress,
                                        LocationCoordinates location) {
        FoodDonation donation = new FoodDonation();
        donation.setDonor(donor);
        donation.setFoodItems(items);
        donation.setPickupAddress(pickupAddress);
        donation.setLocation(location);
        donation.setStatus(DonationStatus.PENDING);
        return donationRepository.save(donation);
    }

    public List<FoodDonation> getDonationsForDonor(Long donorId) {
        return donationRepository.findByDonor_Id(donorId);
    }

    /** Donations any NGO can browse and accept (still unassigned). */
    public List<FoodDonation> getPendingUnassignedDonations() {
        return donationRepository.findByStatusOrderByCreatedAtAsc(DonationStatus.PENDING);
    }

    public List<FoodDonation> getDonationsForNgo(Long ngoId) {
        return donationRepository.findByNgo_Id(ngoId);
    }

    public List<FoodDonation> getDonationsForVolunteer(Long volunteerId) {
        return donationRepository.findByVolunteer_Id(volunteerId);
    }

    @Transactional
    public void acceptDonation(FoodDonation donation, NgoOrg ngo) {
        donation.setNgo(ngo);
        donation.setStatus(DonationStatus.ACCEPTED);
        donationRepository.save(donation);
    }

    @Transactional
    public void declineDonation(FoodDonation donation) {
        donation.setStatus(DonationStatus.DECLINED);
        donationRepository.save(donation);
    }

    @Transactional
    public void assignVolunteer(FoodDonation donation, VolunteerProfile volunteer) {
        donation.setVolunteer(volunteer);
        donation.setStatus(DonationStatus.IN_TRANSIT);
        donationRepository.save(donation);
    }

    @Transactional
    public void confirmDelivery(FoodDonation donation, String remarks) {
        donation.setStatus(DonationStatus.DELIVERED);
        donationRepository.save(donation);

        VolunteerProfile volunteer = donation.getVolunteer();
        volunteer.setCompletedDeliveries(volunteer.getCompletedDeliveries() + 1);
        volunteerProfileRepository.save(volunteer);

        Distribution distribution = new Distribution(donation, donation.getNgo(), volunteer,
                DonationStatus.DELIVERED, remarks);
        distributionRepository.save(distribution);
    }

    // ---- Admin / dashboard statistics (equivalent to the original console app's
    //      "STREAMS API DEMO" section: filtering, mapping, grouping) ----

    public long countByStatus(DonationStatus status) {
        return donationRepository.countByStatus(status);
    }

    public double getTotalFoodRescued() {
        return donationRepository.findAll().stream()
                .filter(d -> d.getStatus() == DonationStatus.DELIVERED)
                .mapToDouble(FoodDonation::getTotalQuantity)
                .sum();
    }

    public Map<DonationStatus, Long> getDonationCountsByStatus() {
        return donationRepository.findAll().stream()
                .collect(Collectors.groupingBy(FoodDonation::getStatus, Collectors.counting()));
    }

    public List<NgoOrg> getUnverifiedNgos() {
        return ngoOrgRepository.findByVerified(false);
    }

    public List<FoodDonation> getAllDonations() {
        return donationRepository.findAll();
    }

    public FoodDonation getById(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Donation not found: " + id));
    }
}
