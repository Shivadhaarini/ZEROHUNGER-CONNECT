package com.zerohunger.backend.entity;

import jakarta.persistence.Embeddable;

/**
 * LocationCoordinates - carried over from the original inner class,
 * now an @Embeddable so it stores directly on the FoodDonation row
 * (pickup + dropoff lat/lon columns).
 */
@Embeddable
public class LocationCoordinates {

    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;

    public LocationCoordinates() {}

    public LocationCoordinates(Double pickupLat, Double pickupLon, Double dropoffLat, Double dropoffLon) {
        this.pickupLatitude = pickupLat;
        this.pickupLongitude = pickupLon;
        this.dropoffLatitude = dropoffLat;
        this.dropoffLongitude = dropoffLon;
    }

    public Double getPickupLatitude() { return pickupLatitude; }
    public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }

    public Double getPickupLongitude() { return pickupLongitude; }
    public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }

    public Double getDropoffLatitude() { return dropoffLatitude; }
    public void setDropoffLatitude(Double dropoffLatitude) { this.dropoffLatitude = dropoffLatitude; }

    public Double getDropoffLongitude() { return dropoffLongitude; }
    public void setDropoffLongitude(Double dropoffLongitude) { this.dropoffLongitude = dropoffLongitude; }

    @Override
    public String toString() {
        if (pickupLatitude == null) return "No coordinates set";
        return String.format("Pickup (%.4f, %.4f) -> Dropoff (%.4f, %.4f)",
                pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude);
    }
}
