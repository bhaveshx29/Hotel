package org.bhavesh.model;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;

    public Customer(int customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Methods from class diagram
    public void searchRoom() {
        // Placeholder: To be implemented with Room class
        System.out.println("Customer " + name + " is searching for a room.");
    }

    public void makePayment(Payment payment) {
        // Placeholder: To be implemented with Payment class
        System.out.println("Customer " + name + " made a payment of $" + payment.getAmount());
    }

    public void provideFeedback() {
        System.out.println("Customer " + name + " provided feedback.");
    }

    public void viewBookingHistory() {
        // Placeholder: To be implemented with Booking class
        System.out.println("Customer " + name + " viewed booking history.");
    }

    public void requestPickup() {
        // Placeholder: To be implemented with PickupService class
        System.out.println("Customer " + name + " requested a pickup.");
    }

    public void cancelBooking(Booking booking) {
        // Placeholder: To be implemented with Booking class
        System.out.println("Customer " + name + " canceled booking ID " + booking.getBookingId());
    }
}