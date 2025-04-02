package org.bhavesh.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/hotel_booking_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // ✅ Add this method (you forgot it!)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                System.out.println("Database Connection Successful!");
                connection.close(); // Close after checking
            } else {
                System.out.println("Database Connection Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
