package org.bhavesh.dao;

import org.bhavesh.model.Customer;
import org.bhavesh.model.PickupService;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PickupServiceDAO {
    private Connection connection;
    private CustomerDAO customerDAO;

    public PickupServiceDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.customerDAO = new CustomerDAO();
    }

    public void schedulePickup(PickupService pickupService) {
        String query = "INSERT INTO pickup_services (customer_id, pickup_location, pickup_date, vehicle_info) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, pickupService.getCustomer().getCustomerId());
            statement.setString(2, pickupService.getPickupLocation());
            statement.setTimestamp(3, new java.sql.Timestamp(pickupService.getPickupDate().getTime()));
            statement.setString(4, pickupService.getVehicleInfo());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                pickupService.setPickupId(resultSet.getInt(1));
            }
            pickupService.schedulePickup();
        } catch (SQLException e) {
            System.out.println("Error scheduling pickup: " + e.getMessage());
        }
    }

    public ArrayList<PickupService> getPickupServicesByCustomer(int customerId) {
        ArrayList<PickupService> pickupServices = new ArrayList<>();
        String query = "SELECT * FROM pickup_services WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(customerId);
                PickupService pickupService = new PickupService(
                        resultSet.getInt("pickup_id"),
                        customer,
                        resultSet.getString("pickup_location"),
                        resultSet.getTimestamp("pickup_date"),
                        resultSet.getString("vehicle_info")
                );
                pickupServices.add(pickupService);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching pickup services: " + e.getMessage());
        }
        return pickupServices;
    }

    public ArrayList<PickupService> getAllPickupServices() {
        ArrayList<PickupService> pickupServices = new ArrayList<>();
        String query = "SELECT * FROM pickup_services";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                PickupService pickupService = new PickupService(
                        resultSet.getInt("pickup_id"),
                        customer,
                        resultSet.getString("pickup_location"),
                        resultSet.getTimestamp("pickup_date"),
                        resultSet.getString("vehicle_info")
                );
                pickupServices.add(pickupService);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all pickup services: " + e.getMessage());
        }
        return pickupServices;
    }
}