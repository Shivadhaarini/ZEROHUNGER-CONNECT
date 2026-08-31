package com.zerohunger.backend.entity;

import jakarta.persistence.*;

/**
 * NgoOrg - matches "NGO Table: ngo_id, ngo_name, contact, address,
 * verification_status" from the report, plus focusArea from the original
 * console NGO class.
 */
@Entity
@Table(name = "ngo")
public class NgoOrg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", unique = true, nullable = false)
    private AppUser appUser;

    private String contact;
    private String address;
    private boolean verified = false; // set true by Admin
    private String focusArea; // e.g. Community Kitchen, Food Bank

    public NgoOrg() {}

    public NgoOrg(AppUser appUser, String contact, String address, boolean verified, String focusArea) {
        this.appUser = appUser;
        this.contact = contact;
        this.address = address;
        this.verified = verified;
        this.focusArea = focusArea;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getAppUser() { return appUser; }
    public void setAppUser(AppUser appUser) { this.appUser = appUser; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getFocusArea() { return focusArea; }
    public void setFocusArea(String focusArea) { this.focusArea = focusArea; }

    public String getRoleDetails() {
        return String.format("NGO: %s | Verified: %s | Focus: %s | Location: %s",
                appUser.getFullName(), verified ? "Yes" : "No", focusArea, address);
    }
}
