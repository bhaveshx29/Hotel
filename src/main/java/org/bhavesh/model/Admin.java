package org.bhavesh.model;

public class Admin {
    private int adminId;
    private String username;
    private String password;

    public Admin(int adminId, String username, String password) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Methods from class diagram
    public void validateCredentials() {
        System.out.println("Admin credentials validated for " + username);
    }

    public void authorizeAccess() {
        System.out.println("Access authorized for admin " + username);
    }

    public void updateRoomAvailability() {
        // Placeholder: To be implemented with Room class
        System.out.println("Admin " + username + " updated room availability.");
    }

    public void generateReports() {
        System.out.println("Admin " + username + " generated reports.");
    }

    public void manageEmployees() {
        // Placeholder: To be implemented with Employee class
        System.out.println("Admin " + username + " is managing employees.");
    }

    public void assignRoles() {
        // Placeholder: To be implemented with Employee class
        System.out.println("Admin " + username + " assigned roles to employees.");
    }

    public void manageRooms() {
        // Placeholder: To be implemented with Room class
        System.out.println("Admin " + username + " is managing rooms.");
    }

    public void managePickupService() {
        // Placeholder: To be implemented with PickupService class
        System.out.println("Admin " + username + " is managing pickup service.");
    }
}