package com.zerohunger.backend.entity;

/**
 * Lifecycle status of a food donation.
 * Mirrors the "Pending" / "In-Transit" / "Delivered" String status used in
 * the original console demo, now as a type-safe enum.
 */
public enum DonationStatus {
    PENDING,
    ACCEPTED,
    IN_TRANSIT,
    DELIVERED,
    DECLINED
}
