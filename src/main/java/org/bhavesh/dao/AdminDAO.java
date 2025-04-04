package org.bhavesh.dao;

import org.bhavesh.model.Admin;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    private Connection connection;

    public AdminDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public Admin validateAdmin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'ADMIN'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Admin admin = new Admin(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                admin.validateCredentials();
                admin.authorizeAccess();
                return admin;
            }
        } catch (SQLException e) {
            System.out.println("Error validating admin: " + e.getMessage());
        }
        return null;
    }
}