package org.bhavesh.gui;

import org.bhavesh.dao.RoomDAO;
import org.bhavesh.model.Room;
import org.bhavesh.utility.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoomManagement extends JFrame {
    private RoomDAO roomDAO;
    private JTable roomTable;
    private DefaultTableModel tableModel;

    // Search and Filter Components
    private JTextField searchField;
    private JComboBox<String> availabilityFilter;

    // Sorting Components
    private JComboBox<String> sortCriteriaDropdown;
    private JButton sortAscendingButton;
    private JButton sortDescendingButton;

    public RoomManagement() {
        Connection connection = DatabaseConnection.getConnection();
        this.roomDAO = new RoomDAO(connection);
        setTitle("Room Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        initUI();
        loadRooms(); // Load all rooms initially
    }

    private void initUI() {
        // Define the color scheme
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color inputFieldColor = new Color(74, 74, 74); // Medium gray (#4A4A4A)
        Color buttonColor = new Color(0, 196, 180); // Cyan (#00C4B4)
        Color tableHeaderColor = new Color(60, 60, 60); // Slightly lighter gray for table header

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Search and Filter Panel
        JPanel searchFilterPanel = new JPanel(new BorderLayout());
        searchFilterPanel.setBackground(backgroundColor);
        searchFilterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        searchField = new JTextField();
        searchField.setBackground(inputFieldColor);
        searchField.setForeground(textColor);
        searchField.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        searchField.setCaretColor(textColor);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(buttonColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Availability Filter Combo Box
        String[] availabilityOptions = {"All", "Available", "Not Available"};
        availabilityFilter = new JComboBox<>(availabilityOptions);
        availabilityFilter.setBackground(inputFieldColor);
        availabilityFilter.setForeground(textColor);
        availabilityFilter.addActionListener(e -> applyFilters());

        // Add components to the search/filter panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(backgroundColor);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchFilterPanel.add(searchPanel, BorderLayout.CENTER);
        searchFilterPanel.add(availabilityFilter, BorderLayout.EAST);

        mainPanel.add(searchFilterPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"Room ID", "Room Type", "Price Per Night", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);
        roomTable.setBackground(backgroundColor); // Set table background to dark gray
        roomTable.setForeground(textColor);
        roomTable.setGridColor(inputFieldColor); // Grid lines match input field color
        roomTable.setSelectionBackground(buttonColor); // Cyan selection background
        roomTable.setSelectionForeground(Color.WHITE);

        // Style the table header
        roomTable.getTableHeader().setBackground(tableHeaderColor);
        roomTable.getTableHeader().setForeground(textColor);
        roomTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        // Custom renderer to handle cell styling, including the background
        roomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set the background for all cells
                if (!isSelected) {
                    c.setBackground(backgroundColor); // Ensure non-selected cells are dark gray
                }

                // Handle the "Availability" column
                if (column == 3) {
                    Boolean isAvailable = (Boolean) tableModel.getValueAt(row, 3);
                    if (isAvailable != null) {
                        setText(isAvailable ? "✓" : "✗");
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setForeground(isAvailable ? Color.GREEN : Color.RED);
                    } else {
                        setText("");
                        setForeground(textColor);
                    }
                } else {
                    setForeground(textColor);
                }
                return c;
            }
        });

        // Create the scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBackground(backgroundColor); // Set scroll pane background
        scrollPane.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));

        // Set the viewport background (area behind the table) to dark gray
        scrollPane.getViewport().setBackground(backgroundColor);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Add Buttons for CRUD Operations
        JButton addButton = new JButton("Add Room");
        JButton updateButton = new JButton("Update Availability");
        JButton deleteButton = new JButton("Delete Room");

        // Style the buttons
        JButton[] buttons = {addButton, updateButton, deleteButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        // Sorting Dropdown
        String[] sortCriteria = {"Sort By", "Room Type", "Price Per Night", "Availability"};
        sortCriteriaDropdown = new JComboBox<>(sortCriteria);
        sortCriteriaDropdown.setBackground(inputFieldColor);
        sortCriteriaDropdown.setForeground(textColor);

        // Sorting Buttons
        sortAscendingButton = new JButton("Sort Ascending");
        sortDescendingButton = new JButton("Sort Descending");

        JButton[] sortButtons = {sortAscendingButton, sortDescendingButton};
        for (JButton button : sortButtons) {
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        // Add Action Listeners for Sorting
        sortAscendingButton.addActionListener(e -> sortRooms(true));
        sortDescendingButton.addActionListener(e -> sortRooms(false));

        // Add components to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sortCriteriaDropdown);
        buttonPanel.add(sortAscendingButton);
        buttonPanel.add(sortDescendingButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add Action Listeners
        searchButton.addActionListener(e -> applyFilters());
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoomAvailability());
        deleteButton.addActionListener(e -> deleteRoom());

        add(mainPanel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void applyFilters() {
        String query = searchField.getText().trim();
        String availabilityOption = (String) availabilityFilter.getSelectedItem();

        try {
            List<Room> rooms = roomDAO.getAllRooms(); // Fetch all rooms from the database

            // Apply filters
            tableModel.setRowCount(0); // Clear existing rows
            for (Room room : rooms) {
                boolean matchesSearch = room.getRoomType().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(room.getPricePerNight()).contains(query) ||
                        String.valueOf(room.getRoomId()).contains(query);

                boolean matchesAvailability = availabilityOption.equals("All") ||
                        (availabilityOption.equals("Available") && room.isAvailability()) ||
                        (availabilityOption.equals("Not Available") && !room.isAvailability());

                if (matchesSearch && matchesAvailability) {
                    Object[] rowData = {room.getRoomId(), room.getRoomType(), room.getPricePerNight(), room.isAvailability()};
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error applying filters: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortRooms(boolean ascending) {
        String selectedCriteria = (String) sortCriteriaDropdown.getSelectedItem();
        if (selectedCriteria == null || selectedCriteria.equals("Sort By")) {
            JOptionPane.showMessageDialog(this, "Please select a sorting criteria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get data from the table model
        int rowCount = tableModel.getRowCount();
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            int roomId = (int) tableModel.getValueAt(i, 0);
            String roomType = (String) tableModel.getValueAt(i, 1);
            double pricePerNight = (double) tableModel.getValueAt(i, 2);
            boolean availability = (boolean) tableModel.getValueAt(i, 3);
            rooms.add(new Room(roomId, roomType, pricePerNight, availability));
        }

        // Sort based on the selected criteria
        Comparator<Room> comparator = null;
        if (selectedCriteria.equals("Room Type")) {
            comparator = Comparator.comparing(Room::getRoomType);
        } else if (selectedCriteria.equals("Price Per Night")) {
            comparator = Comparator.comparingDouble(Room::getPricePerNight);
        } else if (selectedCriteria.equals("Availability")) {
            comparator = Comparator.comparing(Room::isAvailability);
        }

        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
            }
            rooms.sort(comparator);

            // Update the table model with sorted data
            tableModel.setRowCount(0);
            for (Room room : rooms) {
                Object[] rowData = {room.getRoomId(), room.getRoomType(), room.getPricePerNight(), room.isAvailability()};
                tableModel.addRow(rowData);
            }
        }
    }

    private void loadRooms() {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            tableModel.setRowCount(0); // Clear existing rows
            for (Room room : rooms) {
                Object[] rowData = {room.getRoomId(), room.getRoomType(), room.getPricePerNight(), room.isAvailability()};
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRoom() {
        JTextField roomTypeField = new JTextField();
        JTextField priceField = new JTextField();
        JCheckBox availableCheckBox = new JCheckBox("Available");

        Object[] message = {
                "Room Type:", roomTypeField,
                "Price:", priceField,
                "Availability:", availableCheckBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Room", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String roomType = roomTypeField.getText();
                double price = Double.parseDouble(priceField.getText());
                boolean isAvailable = availableCheckBox.isSelected();

                Room room = new Room(0, roomType, price, isAvailable); // roomId will be set by the database
                roomDAO.addRoom(room);
                loadRooms(); // Refresh table
                JOptionPane.showMessageDialog(this, "Room added successfully!");
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input or database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateRoomAvailability() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int roomId = (int) roomTable.getValueAt(selectedRow, 0);
        boolean isAvailable = (boolean) roomTable.getValueAt(selectedRow, 3);

        int option = JOptionPane.showConfirmDialog(this, "Toggle availability for Room ID " + roomId + "?", "Update Availability", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                roomDAO.updateRoomAvailability(roomId, !isAvailable);
                loadRooms(); // Refresh table
                JOptionPane.showMessageDialog(this, "Room availability updated successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int roomId = (int) roomTable.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Room ID " + roomId + "?", "Delete Room", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                roomDAO.deleteRoom(roomId);
                loadRooms(); // Refresh table
                JOptionPane.showMessageDialog(this, "Room deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}