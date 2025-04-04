package org.bhavesh.gui;

import org.bhavesh.dao.BookingDAO;
import org.bhavesh.dao.CustomerDAO;
import org.bhavesh.model.Booking;
import org.bhavesh.model.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomerInfoFrame extends JFrame {
    private CustomerDAO customerDAO;
    private BookingDAO bookingDAO;
    private JTable customerTable;

    public CustomerInfoFrame() {
        setTitle("Hotel Management - Customer Info");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        customerDAO = new CustomerDAO();
        bookingDAO = new BookingDAO();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(28, 36, 40)); // Dark background

        JLabel titleLabel = new JLabel("CUSTOMER INFO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        titleLabel.setForeground(new Color(178, 235, 242));
        titleLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"ID", "Name", "Email", "Phone", "Booking Count"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("Roboto", Font.PLAIN, 14));
        customerTable.setRowHeight(28);
        customerTable.setBackground(new Color(60, 63, 65)); // Dark gray background
        customerTable.setForeground(Color.WHITE);
        customerTable.setGridColor(new Color(69, 90, 100));
        customerTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        customerTable.getTableHeader().setBackground(new Color(38, 50, 56));
        customerTable.getTableHeader().setForeground(Color.WHITE);
        customerTable.setSelectionBackground(new Color(99, 110, 114)); // Row highlight

        // Populate table
        ArrayList<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            int bookingCount = bookingDAO.getBookingsByCustomer(customer.getCustomerId()).size();
            tableModel.addRow(new Object[]{
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    bookingCount
            });
        }

        // Selection Listener
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {
                    Customer selectedCustomer = customers.get(selectedRow);
                    showCustomerDetails(selectedCustomer);
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(customerTable);
        tableScrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        tableScrollPane.getViewport().setBackground(new Color(28, 36, 40)); // Matching background
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Back Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(28, 36, 40)); // Matching background
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Roboto", Font.BOLD, 16));
        backButton.setBackground(new Color(50, 60, 68)); // Dark button
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(100, 120, 130), 1));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void showCustomerDetails(Customer customer) {
        ArrayList<Booking> bookings = bookingDAO.getBookingsByCustomer(customer.getCustomerId());
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(customer.getName()).append("\n");
        details.append("Email: ").append(customer.getEmail()).append("\n");
        details.append("Phone: ").append(customer.getPhone()).append("\n\n");
        details.append("Booking History:\n");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Booking booking : bookings) {
            details.append("Booking ID: ").append(booking.getBookingId()).append("\n");
            details.append("Room: ").append(booking.getRoom().getRoomId())
                    .append(" (").append(booking.getRoom().getRoomType()).append(")\n");
            details.append("Check-In: ").append(dateFormat.format(booking.getCheckInDate())).append("\n");
            details.append("Check-Out: ").append(dateFormat.format(booking.getCheckOutDate())).append("\n");
            details.append("Status: ").append(booking.getStatus()).append("\n\n");
        }

        JTextArea detailArea = new JTextArea(details.toString());
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailArea.setBackground(new Color(46, 52, 54)); // Darker background
        detailArea.setForeground(Color.WHITE);
        detailArea.setMargin(new Insets(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, new JScrollPane(detailArea),
                "Customer Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
