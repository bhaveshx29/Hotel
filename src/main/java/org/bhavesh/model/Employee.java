package org.bhavesh.model;

public class Employee {
    private int employeeId;
    private String name;
    private String role;

    public Employee(int employeeId, String name, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}