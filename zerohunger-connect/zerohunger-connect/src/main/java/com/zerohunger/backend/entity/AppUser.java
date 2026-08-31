package com.zerohunger.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * AppUser - the login/auth record for every person in the system.
 *
 * In the original console app, "User" was an abstract superclass extended
 * by Donor/NGO/Volunteer. Here that inheritance is replaced by composition:
 * AppUser holds login credentials + role, and each role-specific entity
 * (Donor, NgoOrg, VolunteerProfile) holds a one-to-one link back to it plus
 * its own extra fields (address, businessType, focusArea, etc.) - exactly
 * the fields your original subclasses added on top of User.
 */
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password; // BCrypt-hashed, never stored in plain text

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enabled = true;

    public AppUser() {}

    public AppUser(String username, String password, String fullName, String email, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    /**
     * Role summary string - equivalent to the abstract getRoleDetails()
     * polymorphic method in the original console app.
     */
    public String getRoleDetails() {
        return String.format("%s: %s (%s)", role, fullName, email);
    }

    @Override
    public String toString() {
        return String.format("AppUser[id=%d, username=%s, role=%s]", id, username, role);
    }
}
