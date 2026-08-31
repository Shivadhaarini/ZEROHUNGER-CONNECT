package com.zerohunger.backend.repository;

import com.zerohunger.backend.entity.AppUser;
import com.zerohunger.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<AppUser> findByRole(Role role);
}
