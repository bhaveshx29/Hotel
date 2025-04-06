package org.bhavesh.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerLoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public CustomerLoginFrame() {
        setTitle("Customer Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        emailField = createStyledField();
        passwordField = createStyledPasswordField();

        panel.add(getFormLabel("📧 Email:"));
        panel.add(emailField);
        panel.add(getFormLabel("🔐 Password:"));
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        styleBtn(loginBtn);
        styleBtn(registerBtn);

        panel.add(loginBtn);
        panel.add(registerBtn);
        add(panel);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        setVisible(true);
    }

    private JLabel getFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 200, 255)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 200, 255)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private void styleBtn(JButton btn) {
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.CYAN);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Email and password are required!");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "")) {
            PreparedStatement ps = conn.prepareStatement("SELECT customer_id, name FROM customers WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String name = rs.getString("name");

                JOptionPane.showMessageDialog(this, "✅ Welcome back, " + name + "!");
                dispose();

                CustomerDashboardFrame frame = new CustomerDashboardFrame(customerId);
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid login. Try again!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void register() {
        new CustomerRegisterFrame(); // Open the separate registration window
    }

}
