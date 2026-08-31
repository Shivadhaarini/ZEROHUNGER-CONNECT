package com.zerohunger.backend.repository;

import com.zerohunger.backend.entity.DonationStatus;
import com.zerohunger.backend.entity.FoodDonation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodDonationRepository extends JpaRepository<FoodDonation, Long> {
    List<FoodDonation> findByDonor_Id(Long donorId);
    List<FoodDonation> findByNgo_Id(Long ngoId);
    List<FoodDonation> findByVolunteer_Id(Long volunteerId);
    List<FoodDonation> findByStatus(DonationStatus status);
    List<FoodDonation> findByStatusOrderByCreatedAtAsc(DonationStatus status);
    long countByStatus(DonationStatus status);
}
