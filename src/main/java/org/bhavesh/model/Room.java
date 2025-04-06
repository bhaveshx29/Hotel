package org.bhavesh.model;

public class Room {
    private int roomId;          // Corresponds to room_id
    private String roomType;     // Corresponds to room_type
    private double pricePerNight; // Corresponds to price_per_night
    private boolean availability; // Corresponds to availability
    private int quantity;

    // Constructor
    public Room(int roomId, String roomType, double pricePerNight, boolean availability) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.availability = availability;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int capacity) {
        this.quantity = capacity;
    }


    // toString method for debugging
    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomType='" + roomType + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", availability=" + availability +
                '}';
    }
}