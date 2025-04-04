package org.bhavesh.dao;

import org.bhavesh.model.Booking;
import org.bhavesh.model.Customer;
import org.bhavesh.model.Room;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingDAO {
    private Connection connection;
    private CustomerDAO customerDAO;
    private RoomDAO roomDAO;

    public BookingDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.customerDAO = new CustomerDAO();
        this.roomDAO = new RoomDAO(connection);
    }

    public void addBooking(Booking booking) {
        String query = "INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE rooms SET availability = 0 WHERE room_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getCustomer().getCustomerId());
            statement.setInt(2, booking.getRoom().getRoomId());
            statement.setDate(3, new java.sql.Date(booking.getCheckInDate().getTime()));
            statement.setDate(4, new java.sql.Date(booking.getCheckOutDate().getTime()));
            statement.setString(5, booking.getStatus());
            statement.executeUpdate();

            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, booking.getRoom().getRoomId());
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error adding booking: " + e.getMessage());
        }
    }

    public ArrayList<Booking> getBookingsByCustomer(int customerId) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(customerId);
                Room room = roomDAO.getRoomById(resultSet.getInt("room_id"));
                Booking booking = new Booking(
                        resultSet.getInt("reservation_id"),
                        customer,
                        room,
                        resultSet.getDate("check_in_date"),
                        resultSet.getDate("check_out_date"),
                        resultSet.getString("status")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }

    public ArrayList<Booking> getActiveBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE status = 'Active'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(resultSet.getInt("customer_id"));
                Room room = roomDAO.getRoomById(resultSet.getInt("room_id"));
                Booking booking = new Booking(
                        resultSet.getInt("reservation_id"),
                        customer,
                        room,
                        resultSet.getDate("check_in_date"),
                        resultSet.getDate("check_out_date"),
                        resultSet.getString("status")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching active bookings: " + e.getMessage());
        }
        return bookings;
    }

    public void cancelBooking(int bookingId) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        String updateQuery = "UPDATE rooms SET availability = 1 WHERE room_id = (SELECT room_id FROM reservations WHERE reservation_id = ?)";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, bookingId);
            updateStmt.executeUpdate();

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookingId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error canceling booking: " + e.getMessage());
        }
    }
}