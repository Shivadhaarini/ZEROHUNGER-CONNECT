package com.zerohunger.backend.entity;

import jakarta.persistence.Embeddable;

/**
 * FoodItem - the original console app used a Java 21 record here.
 * JPA @Embeddable can't be a record in a way that maps cleanly with
 * Hibernate across versions, so this is a plain embeddable class with the
 * same three fields (type, quantity, expiry).
 */
@Embeddable
public class FoodItem {

    private String type;
    private double quantity;
    private String expiry; // stored as ISO date string, e.g. "2026-12-31"

    public FoodItem() {}

    public FoodItem(String type, double quantity, String expiry) {
        this.type = type;
        this.quantity = quantity;
        this.expiry = expiry;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getExpiry() { return expiry; }
    public void setExpiry(String expiry) { this.expiry = expiry; }

    @Override
    public String toString() {
        return String.format("%s (%.1f kg, expires: %s)", type, quantity, expiry);
    }
}
