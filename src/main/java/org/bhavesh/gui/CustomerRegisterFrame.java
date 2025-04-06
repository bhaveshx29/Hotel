package org.bhavesh.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerRegisterFrame extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;

    public CustomerRegisterFrame() {
        setTitle("Customer Registration");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 30));

        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Styled input fields
        nameField = createStyledField();
        emailField = createStyledField();
        phoneField = createStyledField();
        passwordField = createStyledPasswordField();

        panel.add(getFormLabel("👤 Name:")); panel.add(nameField);
        panel.add(getFormLabel("📧 Email:")); panel.add(emailField);
        panel.add(getFormLabel("📞 Phone:")); panel.add(phoneField);
        panel.add(getFormLabel("🔐 Password:")); panel.add(passwordField);

        JButton registerBtn = new JButton("Register");
        styleBtn(registerBtn);
        panel.add(new JLabel()); // Spacer
        panel.add(registerBtn);

        registerBtn.addActionListener(e -> register());

        add(panel);
        setVisible(true);
    }

    // 🔵 Label styling
    private JLabel getFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    // 🔵 Input styling
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

    // 🔵 Button styling
    private void styleBtn(JButton btn) {
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.CYAN);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    // 🔵 Custom styled message popup
    private void showStyledMessage(Component parent, String message) {
        UIManager.put("OptionPane.background", new Color(30, 30, 30));
        UIManager.put("Panel.background", new Color(30, 30, 30));
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.CYAN);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.font", new Font("Segoe UI", Font.BOLD, 14));

        JOptionPane.showMessageDialog(parent, message, "📢 Message", JOptionPane.INFORMATION_MESSAGE);
    }

    // ✅ Register logic
    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showStyledMessage(this, "❌ All fields are required!");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "")) {
            PreparedStatement check = conn.prepareStatement("SELECT * FROM customers WHERE email=?");
            check.setString(1, email);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                showStyledMessage(this, "⚠️ Email already exists. Please login.");
                return;
            }

            String insert = "INSERT INTO customers (name, email, phone, password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.executeUpdate();

            showStyledMessage(this, "✅ Registration successful! You can now log in.");
            dispose(); // Close register screen
        } catch (Exception e) {
            e.printStackTrace();
            showStyledMessage(this, "❌ Error: " + e.getMessage());
        }
    }
}
