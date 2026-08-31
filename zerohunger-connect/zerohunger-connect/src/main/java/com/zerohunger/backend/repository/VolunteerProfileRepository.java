package com.zerohunger.backend.repository;

import com.zerohunger.backend.entity.VolunteerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfile, Long> {
    Optional<VolunteerProfile> findByAppUser_Username(String username);
    List<VolunteerProfile> findByAvailable(boolean available);
}
