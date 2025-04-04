package org.bhavesh.model;

import java.util.Date;

public class PickupService {
    private int pickupId;
    private Customer customer; // Composition
    private String pickupLocation;
    private Date pickupDate; // Using Date instead of DateTime for simplicity
    private String vehicleInfo;

    public PickupService(int pickupId, Customer customer, String pickupLocation, Date pickupDate, String vehicleInfo) {
        this.pickupId = pickupId;
        this.customer = customer;
        this.pickupLocation = pickupLocation;
        this.pickupDate = pickupDate;
        this.vehicleInfo = vehicleInfo;
    }

    // Getters and Setters
    public int getPickupId() {
        return pickupId;
    }

    public void setPickupId(int pickupId) {
        this.pickupId = pickupId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    // Methods from class diagram
    public void schedulePickup() {
        System.out.println("Pickup scheduled for customer ID " + customer.getCustomerId());
    }

    public void addVehiclesToFleet() {
        System.out.println("Vehicle added to fleet for pickup ID " + pickupId);
    }
}