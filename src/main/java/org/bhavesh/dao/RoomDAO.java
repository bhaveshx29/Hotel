package org.bhavesh.dao;

import org.bhavesh.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection connection;

    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new room to the database
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (room_id, room_type, price_per_night, availability) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, room.getRoomId());
            stmt.setString(2, room.getRoomType());
            stmt.setDouble(3, room.getPricePerNight());
            stmt.setBoolean(4, room.isAvailability());
            stmt.executeUpdate();
        }
    }

    // Retrieve a room by its ID
    public Room getRoomById(int roomId) throws SQLException {
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night"),
                        rs.getBoolean("availability")
                );
            }
        }
        return null;
    }

    // Update the availability status of a room
    public void updateRoomAvailability(int roomId, boolean availability) throws SQLException {
        String query = "UPDATE rooms SET availability = ? WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, availability);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        }
    }

    // Delete a room from the database
    public void deleteRoom(int roomId) throws SQLException {
        String query = "DELETE FROM rooms WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
        }
    }

    // Retrieve all rooms from the database
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night"),
                        rs.getBoolean("availability")
                ));
            }
        }
        return rooms;
    }

    // Add method to get available rooms (needed by other parts of the system)
    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE availability = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night"),
                        rs.getBoolean("availability")
                ));
            }
        }
        return rooms;
    }
}