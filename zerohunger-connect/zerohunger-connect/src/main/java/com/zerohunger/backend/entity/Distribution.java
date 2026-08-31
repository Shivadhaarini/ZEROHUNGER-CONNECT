package com.zerohunger.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Distribution - matches "Distribution Table: distribution_id,
 * donation_id, ngo_id, volunteer_id, delivery_status, delivery_date,
 * remarks" from the project report. Created once a volunteer confirms
 * pickup, and updated as the final delivery is completed.
 */
@Entity
@Table(name = "distribution")
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donation_id", nullable = false, unique = true)
    private FoodDonation donation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ngo_id", nullable = false)
    private NgoOrg ngo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private VolunteerProfile volunteer;

    @Enumerated(EnumType.STRING)
    private DonationStatus deliveryStatus;

    private LocalDateTime deliveryDate;

    @Column(length = 500)
    private String remarks;

    public Distribution() {}

    public Distribution(FoodDonation donation, NgoOrg ngo, VolunteerProfile volunteer,
                         DonationStatus deliveryStatus, String remarks) {
        this.donation = donation;
        this.ngo = ngo;
        this.volunteer = volunteer;
        this.deliveryStatus = deliveryStatus;
        this.remarks = remarks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FoodDonation getDonation() { return donation; }
    public void setDonation(FoodDonation donation) { this.donation = donation; }

    public NgoOrg getNgo() { return ngo; }
    public void setNgo(NgoOrg ngo) { this.ngo = ngo; }

    public VolunteerProfile getVolunteer() { return volunteer; }
    public void setVolunteer(VolunteerProfile volunteer) { this.volunteer = volunteer; }

    public DonationStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DonationStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        this.deliveryDate = LocalDateTime.now();
    }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
