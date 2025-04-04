package org.bhavesh.model;

import java.util.Date;

public class Booking {
    private int bookingId;
    private Customer customer; // Composition
    private Room room;        // Composition
    private Date checkInDate;
    private Date checkOutDate;
    private String status;

    public Booking(int bookingId, Customer customer, Room room, Date checkInDate, Date checkOutDate, String status) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Methods from class diagram
    public void sendConfirmationMail() {
        System.out.println("Confirmation mail sent for booking ID " + bookingId);
    }

    public void modifyBooking() {
        System.out.println("Booking ID " + bookingId + " modified.");
    }
}