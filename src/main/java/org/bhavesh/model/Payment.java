package org.bhavesh.model;

public class Payment {
    private int paymentId;
    private Customer customer; // Composition
    private double amount;
    private String paymentMethod;
    private String paymentStatus;

    public Payment(int paymentId, Customer customer, double amount, String paymentMethod, String paymentStatus) {
        this.paymentId = paymentId;
        this.customer = customer;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Methods from class diagram
    public void processBilling() {
        System.out.println("Processing billing for payment ID " + paymentId);
    }

    public void generateReceipt() {
        System.out.println("Receipt generated for payment ID " + paymentId);
    }
}