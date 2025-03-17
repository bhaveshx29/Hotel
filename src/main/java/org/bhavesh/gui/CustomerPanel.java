package org.bhavesh.gui;

import org.bhavesh.dao.CustomerDAO;
import org.bhavesh.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {
    private JTable customerTable;
    private JButton addCustomerButton;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Table to display customers
        customerTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button to add a new customer
        addCustomerButton = new JButton("Add Customer");
        add(addCustomerButton, BorderLayout.SOUTH);

        // Load customer data into the table
        loadCustomerData();

        // Add action listener for the "Add Customer" button
        addCustomerButton.addActionListener(e -> {
            // Open a dialog to add a new customer
            // Implement this part
        });
    }

    private void loadCustomerData() {
        try {
            CustomerDAO customerDAO = new CustomerDAO();
            List<Customer> customers = customerDAO.getAllCustomers();

            // Convert the list to a table model
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Name");
            model.addColumn("Email");
            model.addColumn("Phone");

            for (Customer customer : customers) {
                model.addRow(new Object[]{
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone()
                });
            }

            customerTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load customer data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}