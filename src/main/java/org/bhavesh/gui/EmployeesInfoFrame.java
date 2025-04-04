package org.bhavesh.gui;

import org.bhavesh.dao.EmployeeDAO;
import org.bhavesh.model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EmployeesInfoFrame extends JFrame {
    private EmployeeDAO employeeDAO;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeesInfoFrame(String role) {
        setTitle("Hotel Management - Employees Info");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        employeeDAO = new EmployeeDAO();

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
        JLabel titleLabel = new JLabel("EMPLOYEES INFO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        titleLabel.setForeground(textColor);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Employee Table
        String[] columnNames = {"Employee ID", "Name", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setBackground(backgroundColor);
        employeeTable.setForeground(textColor);
        employeeTable.setGridColor(inputFieldColor);
        employeeTable.setSelectionBackground(buttonColor);
        employeeTable.setSelectionForeground(Color.WHITE);
        employeeTable.setRowHeight(30); // Increase row height for better readability
        employeeTable.setFont(new Font("Roboto", Font.PLAIN, 14));

        // Style the table header
        employeeTable.getTableHeader().setBackground(tableHeaderColor);
        employeeTable.getTableHeader().setForeground(textColor);
        employeeTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        employeeTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        // Custom renderer for alternating row colors and alignment
        employeeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        // Load employee data into the table
        updateEmployeeTable();

        // Scroll Pane for the Table
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        scrollPane.getViewport().setBackground(backgroundColor);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // Updated to 3 columns for the Back button
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add Employee Button
        JButton addButton = new JButton("Add Employee");
        addButton.setBackground(buttonColor);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (role.equals("Admin")) {
                    addEmployee();
                } else {
                    JOptionPane.showMessageDialog(EmployeesInfoFrame.this, "Only admins can add employees!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete Employee Button
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setBackground(buttonColor);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (role.equals("Admin")) {
                    deleteEmployee();
                } else {
                    JOptionPane.showMessageDialog(EmployeesInfoFrame.this, "Only admins can delete employees!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void updateEmployeeTable() {
        tableModel.setRowCount(0); // Clear existing rows
        ArrayList<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee employee : employees) {
            Object[] rowData = {employee.getEmployeeId(), employee.getName(), employee.getRole()};
            tableModel.addRow(rowData);
        }
    }

    private void addEmployee() {
        JTextField nameField = new JTextField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"MANAGER", "RECEPTIONIST"});

        // Style the input fields for the dialog
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color inputFieldColor = new Color(74, 74, 74); // Medium gray (#4A4A4A)

        nameField.setBackground(inputFieldColor);
        nameField.setForeground(textColor);
        nameField.setBorder(BorderFactory.createLineBorder(inputFieldColor, 1));
        nameField.setCaretColor(textColor);

        roleComboBox.setBackground(inputFieldColor);
        roleComboBox.setForeground(textColor);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(backgroundColor);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(textColor);
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(textColor);
        panel.add(roleLabel);
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Employee employee = new Employee(0, name, role);
            employeeDAO.addEmployee(employee);
            updateEmployeeTable();
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        employeeDAO.deleteEmployee(employeeId);
        updateEmployeeTable();
    }
}