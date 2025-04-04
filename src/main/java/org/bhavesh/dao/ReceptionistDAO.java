package org.bhavesh.dao;

import org.bhavesh.model.Receptionist;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceptionistDAO {
    private Connection connection;

    public ReceptionistDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public Receptionist validateReceptionist(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'RECEPTIONIST'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Receptionist(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error validating receptionist: " + e.getMessage());
        }
        return null;
    }
}