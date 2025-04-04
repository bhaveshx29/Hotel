package org.bhavesh.gui;

import org.bhavesh.dao.BookingDAO;
import org.bhavesh.dao.CustomerDAO;
import org.bhavesh.dao.RoomDAO;
import org.bhavesh.model.Booking;
import org.bhavesh.model.Customer;
import org.bhavesh.model.Room;
import org.bhavesh.utility.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCustomerFrame extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JComboBox<Room> roomComboBox;
    private JTextField checkInField, checkOutField;
    private RoomDAO roomDAO;
    private CustomerDAO customerDAO;
    private BookingDAO bookingDAO;

    public NewCustomerFrame() {
        setTitle("Hotel Management - New Customer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Connection connection = DatabaseConnection.getConnection();
        roomDAO = new RoomDAO(connection);
        customerDAO = new CustomerDAO();
        bookingDAO = new BookingDAO();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 45, 50));

        // Title
        JLabel titleLabel = new JLabel("NEW CUSTOMER FORM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        titleLabel.setForeground(new Color(178, 235, 242));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center Panel for Form
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(new Color(34, 45, 50));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

        formPanel.add(UIUtils.createLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(UIUtils.createLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(UIUtils.createLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(UIUtils.createLabel("Room:"));
        List<Room> availableRooms = new ArrayList<>();
        try {
            availableRooms = roomDAO.getAvailableRooms();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading available rooms: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        roomComboBox = new JComboBox<>(availableRooms.toArray(new Room[0]));
        formPanel.add(roomComboBox);

        formPanel.add(UIUtils.createLabel("Check-In Date (yyyy-MM-dd):"));
        checkInField = new JTextField();
        formPanel.add(checkInField);

        formPanel.add(UIUtils.createLabel("Check-Out Date (yyyy-MM-dd):"));
        checkOutField = new JTextField();
        formPanel.add(checkOutField);

        JButton bookButton = new JButton("Book");
        UIUtils.styleButton(bookButton, new Color(100, 181, 246));
        bookButton.addActionListener(e -> bookRoom());
        formPanel.add(new JLabel(""));
        formPanel.add(bookButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bottom Panel (Back Button)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(34, 45, 50));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JButton backButton = new JButton("Back");
        UIUtils.styleButton(backButton, new Color(239, 83, 80));
        backButton.addActionListener(e -> dispose());
        bottomPanel.add(backButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void bookRoom() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
        String checkInStr = checkInField.getText().trim();
        String checkOutStr = checkOutField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || checkInStr.isEmpty() || checkOutStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date checkInDate, checkOutDate;
        try {
            checkInDate = dateFormat.parse(checkInStr);
            checkOutDate = dateFormat.parse(checkOutStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer(0, name, email, phone);
        int customerId = customerDAO.addCustomer(customer);
        if (customerId == -1) {
            JOptionPane.showMessageDialog(this, "Failed to add customer!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = new Booking(0, customer, selectedRoom, checkInDate, checkOutDate, "Active");
        bookingDAO.addBooking(booking);
        JOptionPane.showMessageDialog(this, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}