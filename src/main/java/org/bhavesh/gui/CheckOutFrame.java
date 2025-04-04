package org.bhavesh.gui;

import org.bhavesh.dao.BookingDAO;
import org.bhavesh.dao.CustomerDAO;
import org.bhavesh.dao.PaymentDAO;
import org.bhavesh.model.Booking;
import org.bhavesh.model.Customer;
import org.bhavesh.model.Payment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CheckOutFrame extends JFrame {
    private BookingDAO bookingDAO;
    private CustomerDAO customerDAO;
    private PaymentDAO paymentDAO;
    private JComboBox<String> bookingComboBox;

    public CheckOutFrame() {
        setTitle("Hotel Management - Check Out");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        bookingDAO = new BookingDAO();
        customerDAO = new CustomerDAO();
        paymentDAO = new PaymentDAO();

        // Define the color scheme
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color inputFieldColor = new Color(74, 74, 74); // Medium gray (#4A4A4A)
        Color buttonColor = new Color(0, 196, 180); // Cyan (#00C4B4)
        Color formPanelColor = new Color(60, 60, 60); // Slightly lighter gray for form panel

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("CHECK OUT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center Panel for Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formPanelColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Select Booking
        JLabel bookingLabel = new JLabel("Select Booking:");
        bookingLabel.setForeground(textColor);
        bookingLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(bookingLabel, gbc);

        ArrayList<Booking> activeBookings = bookingDAO.getActiveBookings();
        ArrayList<String> bookingOptions = new ArrayList<>();
        for (Booking booking : activeBookings) {
            bookingOptions.add("Booking ID: " + booking.getBookingId() + " - Customer: " + booking.getCustomer().getName());
        }
        bookingComboBox = new JComboBox<>(bookingOptions.toArray(new String[0]));
        bookingComboBox.setBackground(inputFieldColor);
        bookingComboBox.setForeground(textColor);
        bookingComboBox.setFont(new Font("Roboto", Font.PLAIN, 16));
        bookingComboBox.setPreferredSize(new Dimension(300, 35)); // Increased size for better visibility
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(bookingComboBox, gbc);

        // Payment Method
        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentLabel.setForeground(textColor);
        paymentLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(paymentLabel, gbc);

        JComboBox<String> paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card"});
        paymentMethodComboBox.setBackground(inputFieldColor);
        paymentMethodComboBox.setForeground(textColor);
        paymentMethodComboBox.setFont(new Font("Roboto", Font.PLAIN, 16));
        paymentMethodComboBox.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(paymentMethodComboBox, gbc);

        // Check Out Button
        JButton checkOutButton = new JButton("Check Out");
        checkOutButton.setBackground(buttonColor);
        checkOutButton.setForeground(Color.WHITE);
        checkOutButton.setFocusPainted(false);
        checkOutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        checkOutButton.setFont(new Font("Roboto", Font.BOLD, 16));
        checkOutButton.setPreferredSize(new Dimension(150, 40));
        checkOutButton.addActionListener(e -> checkOut(activeBookings, paymentMethodComboBox.getSelectedItem().toString()));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(checkOutButton, gbc);

        // Center the form panel in the main panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.add(formPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel (Back Button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setFont(new Font("Roboto", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.addActionListener(e -> dispose());
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void checkOut(ArrayList<Booking> activeBookings, String paymentMethod) {
        int selectedIndex = bookingComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = activeBookings.get(selectedIndex);
        Customer customer = booking.getCustomer();
        double amount = calculateAmount(booking);
        Payment payment = new Payment(0, customer, amount, paymentMethod, "Completed");
        paymentDAO.addPayment(payment);
        payment.processBilling();
        payment.generateReceipt();

        bookingDAO.cancelBooking(booking.getBookingId());
        customer.cancelBooking(booking);
        JOptionPane.showMessageDialog(this, "Check-out successful! Amount: $" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private double calculateAmount(Booking booking) {
        long diffInMillies = booking.getCheckOutDate().getTime() - booking.getCheckInDate().getTime();
        long days = diffInMillies / (1000 * 60 * 60 * 24);
        return days * booking.getRoom().getPricePerNight();
    }
}