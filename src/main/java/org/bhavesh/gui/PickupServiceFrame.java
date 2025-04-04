package org.bhavesh.gui;

import org.bhavesh.dao.CustomerDAO;
import org.bhavesh.dao.PickupServiceDAO;
import org.bhavesh.model.Customer;
import org.bhavesh.model.PickupService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PickupServiceFrame extends JFrame {
    private CustomerDAO customerDAO;
    private PickupServiceDAO pickupServiceDAO;
    private JComboBox<String> customerComboBox;

    public PickupServiceFrame() {
        setTitle("Pickup Service");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        customerDAO = new CustomerDAO();
        pickupServiceDAO = new PickupServiceDAO();

        // Define the color scheme
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color inputFieldColor = new Color(74, 74, 74); // Medium gray (#4A4A4A)
        Color buttonColor = new Color(0, 196, 180); // Cyan (#00C4B4)

        // Main Panel
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Labels
        JLabel customerLabel = new JLabel("Select Customer:");
        customerLabel.setForeground(textColor);
        panel.add(customerLabel);

        // Customer Combo Box
        ArrayList<Customer> customers = customerDAO.getAllCustomers();
        ArrayList<String> customerOptions = new ArrayList<>();
        for (Customer customer : customers) {
            customerOptions.add(customer.getName() + " (ID: " + customer.getCustomerId() + ")");
        }
        customerComboBox = new JComboBox<>(customerOptions.toArray(new String[0]));
        customerComboBox.setBackground(inputFieldColor);
        customerComboBox.setForeground(textColor);
        panel.add(customerComboBox);

        // Pickup Location
        JLabel locationLabel = new JLabel("Pickup Location:");
        locationLabel.setForeground(textColor);
        panel.add(locationLabel);

        JTextField locationField = new JTextField();
        locationField.setBackground(inputFieldColor);
        locationField.setForeground(textColor);
        locationField.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        locationField.setCaretColor(textColor);
        panel.add(locationField);

        // Pickup Date
        JLabel dateLabel = new JLabel("Pickup Date (yyyy-MM-dd HH:mm):");
        dateLabel.setForeground(textColor);
        panel.add(dateLabel);

        JTextField dateField = new JTextField();
        dateField.setBackground(inputFieldColor);
        dateField.setForeground(textColor);
        dateField.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        dateField.setCaretColor(textColor);
        panel.add(dateField);

        // Vehicle Info
        JLabel vehicleLabel = new JLabel("Vehicle Info:");
        vehicleLabel.setForeground(textColor);
        panel.add(vehicleLabel);

        JTextField vehicleField = new JTextField();
        vehicleField.setBackground(inputFieldColor);
        vehicleField.setForeground(textColor);
        vehicleField.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        vehicleField.setCaretColor(textColor);
        panel.add(vehicleField);

        // Schedule Button
        panel.add(new JLabel("")); // Empty label for grid alignment

        JButton scheduleButton = new JButton("Schedule Pickup");
        scheduleButton.setBackground(buttonColor);
        scheduleButton.setForeground(Color.WHITE);
        scheduleButton.setFocusPainted(false);
        scheduleButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        scheduleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schedulePickup(customers, locationField.getText(), dateField.getText(), vehicleField.getText());
            }
        });
        panel.add(scheduleButton);

        add(panel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void schedulePickup(ArrayList<Customer> customers, String location, String dateStr, String vehicleInfo) {
        int selectedIndex = customerComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (location.isEmpty() || dateStr.isEmpty() || vehicleInfo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date pickupDate;
        try {
            pickupDate = dateFormat.parse(dateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = customers.get(selectedIndex);
        PickupService pickupService = new PickupService(0, customer, location, pickupDate, vehicleInfo);
        pickupServiceDAO.schedulePickup(pickupService);
        customer.requestPickup();
        JOptionPane.showMessageDialog(this, "Pickup scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}