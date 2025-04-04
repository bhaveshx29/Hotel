package org.bhavesh.dao;

import org.bhavesh.model.Employee;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeeDAO {
    private Connection connection;

    public EmployeeDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public int addEmployee(Employee employee) {
        String query = "INSERT INTO users (username, password, role, name) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getName());
            statement.setString(2, "default123");
            statement.setString(3, employee.getRole());
            statement.setString(4, employee.getName());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
                employee.setEmployeeId(generatedId);
            }
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
        return generatedId;
    }

    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role IN ('MANAGER', 'RECEPTIONIST')";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("role")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    public void deleteEmployee(int employeeId) {
        String query = "DELETE FROM users WHERE user_id = ? AND role IN ('MANAGER', 'RECEPTIONIST')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}