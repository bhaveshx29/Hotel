package org.bhavesh.gui;

import org.bhavesh.dao.PickupServiceDAO;
import org.bhavesh.model.PickupService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PickupInfoFrame extends JFrame {
    private PickupServiceDAO pickupServiceDAO;
    private JTable pickupTable;
    private DefaultTableModel tableModel;

    public PickupInfoFrame() {
        setTitle("Pickup Info");
        setSize(800, 600); // Increased size to better fit the table
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pickupServiceDAO = new PickupServiceDAO();

        // Define the color scheme
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color inputFieldColor = new Color(74, 74, 74); // Medium gray (#4A4A4A)
        Color buttonColor = new Color(0, 196, 180); // Cyan (#00C4B4)
        Color tableHeaderColor = new Color(60, 60, 60); // Slightly lighter gray for table header
        Color alternateRowColor = new Color(58, 58, 58); // Slightly lighter gray for alternating rows

        // Main Panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Pickup Services", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 32)); // Increased font size for consistency
        titleLabel.setForeground(textColor);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Pickup Table
        String[] columnNames = {"Pickup ID", "Customer", "Location", "Date", "Vehicle"};
        tableModel = new DefaultTableModel(columnNames, 0);
        pickupTable = new JTable(tableModel);
        pickupTable.setBackground(backgroundColor);
        pickupTable.setForeground(textColor);
        pickupTable.setGridColor(inputFieldColor);
        pickupTable.setSelectionBackground(buttonColor);
        pickupTable.setSelectionForeground(Color.WHITE);
        pickupTable.setRowHeight(30); // Increase row height for better readability
        pickupTable.setFont(new Font("Roboto", Font.PLAIN, 14));

        // Style the table header
        pickupTable.getTableHeader().setBackground(tableHeaderColor);
        pickupTable.getTableHeader().setForeground(textColor);
        pickupTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        pickupTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        // Custom renderer for alternating row colors and alignment
        pickupTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    // Alternating row colors
                    c.setBackground(row % 2 == 0 ? backgroundColor : alternateRowColor);
                }
                setForeground(textColor);
                setHorizontalAlignment(SwingConstants.CENTER); // Center-align text
                return c;
            }
        });

        // Load pickup service data into the table
        updatePickupTable();

        // Scroll Pane for the Table
        JScrollPane scrollPane = new JScrollPane(pickupTable);
        scrollPane.setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        scrollPane.getViewport().setBackground(backgroundColor);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel for Back Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame to return to the previous screen
            }
        });

        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void updatePickupTable() {
        tableModel.setRowCount(0); // Clear existing rows
        ArrayList<PickupService> pickupServices = pickupServiceDAO.getAllPickupServices();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (PickupService pickupService : pickupServices) {
            Object[] rowData = {
                pickupService.getPickupId(),
                pickupService.getCustomer().getName(),
                pickupService.getPickupLocation(),
                dateFormat.format(pickupService.getPickupDate()),
                pickupService.getVehicleInfo()
            };
            tableModel.addRow(rowData);
        }
    }
}