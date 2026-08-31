package com.zerohunger.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * FoodDonation - matches "Food Donation Table: donation_id, food_name,
 * quantity, expiry_time, pickup_address, status" from the report, extended
 * to keep the original console app's Donation class capabilities: multiple
 * FoodItems per donation, pickup/dropoff coordinates, and links to donor,
 * NGO and volunteer.
 */
@Entity
@Table(name = "food_donation")
public class FoodDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id")
    private NgoOrg ngo; // null until an NGO accepts the donation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private VolunteerProfile volunteer; // null until a volunteer is assigned

    @ElementCollection
    @CollectionTable(name = "food_donation_items", joinColumns = @JoinColumn(name = "donation_id"))
    private List<FoodItem> foodItems = new ArrayList<>();

    @Embedded
    private LocationCoordinates location;

    private String pickupAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    public FoodDonation() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
        if (status == null) status = DonationStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }

    public NgoOrg getNgo() { return ngo; }
    public void setNgo(NgoOrg ngo) { this.ngo = ngo; }

    public VolunteerProfile getVolunteer() { return volunteer; }
    public void setVolunteer(VolunteerProfile volunteer) { this.volunteer = volunteer; }

    public List<FoodItem> getFoodItems() { return foodItems; }
    public void setFoodItems(List<FoodItem> foodItems) { this.foodItems = foodItems; }

    public LocationCoordinates getLocation() { return location; }
    public void setLocation(LocationCoordinates location) { this.location = location; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public DonationStatus getStatus() { return status; }
    public void setStatus(DonationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    /**
     * Total quantity across all food items - same calculation as the
     * original console app's getTotalQuantity() stream sum.
     */
    public double getTotalQuantity() {
        return foodItems.stream().mapToDouble(FoodItem::getQuantity).sum();
    }

    /**
     * verifyDonation() - carried over unchanged from the original class.
     */
    public boolean verifyDonation() {
        if (foodItems == null || foodItems.isEmpty()) return false;
        return foodItems.stream().allMatch(item -> item.getQuantity() > 0);
    }

    public String getTrackingStatus() {
        return String.format("Donation #%d: %s (Last updated: %s)", id, status, lastUpdated);
    }

    @Override
    public String toString() {
        return String.format("Donation[id=%d, donor=%s, ngo=%s, status=%s, items=%d, total=%.1f kg]",
                id, donor.getAppUser().getFullName(),
                ngo != null ? ngo.getAppUser().getFullName() : "unassigned",
                status, foodItems.size(), getTotalQuantity());
    }
}
