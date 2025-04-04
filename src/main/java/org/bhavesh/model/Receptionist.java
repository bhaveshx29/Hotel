package org.bhavesh.model;

public class Receptionist {
    private int receptionistId;
    private String username;
    private String password;

    public Receptionist(int receptionistId, String username, String password) {
        this.receptionistId = receptionistId;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(int receptionistId) {
        this.receptionistId = receptionistId;
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
    public void manageCheckIns() {
        // Placeholder: To be implemented with Booking class
        System.out.println("Receptionist " + username + " is managing check-ins.");
    }

    public void manageRoomStatus() {
        // Placeholder: To be implemented with Room class
        System.out.println("Receptionist " + username + " is managing room status.");
    }

    public void managePickupService() {
        // Placeholder: To be implemented with PickupService class
        System.out.println("Receptionist " + username + " is managing pickup service.");
    }
}