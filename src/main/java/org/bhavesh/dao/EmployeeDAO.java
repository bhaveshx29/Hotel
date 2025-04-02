package org.bhavesh.dao;

import org.bhavesh.model.Employee;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.*;

public class EmployeeDAO {
    
    // Add new employee
    public boolean addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (name, email, phone, position, hire_date, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getPosition());
            stmt.setDate(5, java.sql.Date.valueOf(employee.getHireDate()));
            stmt.setInt(6, employee.getUserId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Get employee by user ID
    public Employee getEmployeeByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getInt("user_id")
                    );
                }
            }
        }
        return null;
    }
}