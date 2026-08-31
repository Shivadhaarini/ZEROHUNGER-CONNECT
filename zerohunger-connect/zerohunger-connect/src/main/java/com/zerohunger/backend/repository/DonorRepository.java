package com.zerohunger.backend.repository;

import com.zerohunger.backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findByAppUser_Username(String username);
}
