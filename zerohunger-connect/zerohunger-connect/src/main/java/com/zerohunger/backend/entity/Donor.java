package com.zerohunger.backend.entity;

import jakarta.persistence.*;

/**
 * Donor - matches the "Donor Table: donor_id, donor_name, phone, email,
 * address" schema from the project report, plus businessType carried over
 * from the original console Donor class.
 */
@Entity
@Table(name = "donor")
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", unique = true, nullable = false)
    private AppUser appUser;

    private String phone;
    private String address;
    private String businessType; // e.g. Retail, Agriculture, Restaurant

    public Donor() {}

    public Donor(AppUser appUser, String phone, String address, String businessType) {
        this.appUser = appUser;
        this.phone = phone;
        this.address = address;
        this.businessType = businessType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getAppUser() { return appUser; }
    public void setAppUser(AppUser appUser) { this.appUser = appUser; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getRoleDetails() {
        return String.format("Donor: %s | Business: %s | Location: %s",
                appUser.getFullName(), businessType, address);
    }
}
