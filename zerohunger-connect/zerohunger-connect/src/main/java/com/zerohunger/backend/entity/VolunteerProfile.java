package com.zerohunger.backend.entity;

import jakarta.persistence.*;

/**
 * VolunteerProfile - matches "Volunteer Table: volunteer_id,
 * volunteer_name, area, contact, availability" from the report, plus
 * completedDeliveries carried over from the original console Volunteer
 * class.
 */
@Entity
@Table(name = "volunteer")
public class VolunteerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", unique = true, nullable = false)
    private AppUser appUser;

    private String phone;
    private String area; // assigned zone
    private boolean available = true;
    private int completedDeliveries = 0;

    public VolunteerProfile() {}

    public VolunteerProfile(AppUser appUser, String phone, String area, boolean available, int completedDeliveries) {
        this.appUser = appUser;
        this.phone = phone;
        this.area = area;
        this.available = available;
        this.completedDeliveries = completedDeliveries;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getAppUser() { return appUser; }
    public void setAppUser(AppUser appUser) { this.appUser = appUser; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getCompletedDeliveries() { return completedDeliveries; }
    public void setCompletedDeliveries(int completedDeliveries) { this.completedDeliveries = completedDeliveries; }

    public String getRoleDetails() {
        return String.format("Volunteer: %s | Available: %s | Deliveries: %d | Area: %s",
                appUser.getFullName(), available ? "Yes" : "No", completedDeliveries, area);
    }
}
