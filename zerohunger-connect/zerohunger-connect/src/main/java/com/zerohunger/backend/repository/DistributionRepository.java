package com.zerohunger.backend.repository;

import com.zerohunger.backend.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DistributionRepository extends JpaRepository<Distribution, Long> {
    Optional<Distribution> findByDonation_Id(Long donationId);
}
