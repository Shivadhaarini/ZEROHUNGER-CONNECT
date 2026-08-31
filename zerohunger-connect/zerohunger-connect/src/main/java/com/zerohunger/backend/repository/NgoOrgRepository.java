package com.zerohunger.backend.repository;

import com.zerohunger.backend.entity.NgoOrg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NgoOrgRepository extends JpaRepository<NgoOrg, Long> {
    Optional<NgoOrg> findByAppUser_Username(String username);
    List<NgoOrg> findByVerified(boolean verified);
}
