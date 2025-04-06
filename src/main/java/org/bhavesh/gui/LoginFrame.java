package org.bhavesh.gui;

import org.bhavesh.dao.AdminDAO;
import org.bhavesh.dao.ReceptionistDAO;
import org.bhavesh.model.Admin;
import org.bhavesh.model.Receptionist;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public LoginFrame() {
        setTitle("Hotel Management - Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Color scheme
        Color backgroundColor = new Color(46, 46, 46);
        Color textColor = new Color(211, 211, 211);
        Color inputColor = new Color(74, 74, 74);
        Color btnColor = new Color(0, 196, 180);
        Color formPanelColor = new Color(60, 60, 60);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("🔐 Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(textColor);
        mainPanel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formPanelColor);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(getStyledLabel("Role:", textColor), gbc);

        roleComboBox = new JComboBox<>(new String[]{"Admin", "Receptionist"});
        styleField(roleComboBox, inputColor, textColor);
        gbc.gridx = 1;
        formPanel.add(roleComboBox, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(getStyledLabel("Username:", textColor), gbc);

        usernameField = new JTextField();
        styleField(usernameField, inputColor, textColor);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(getStyledLabel("Password:", textColor), gbc);

        passwordField = new JPasswordField();
        styleField(passwordField, inputColor, textColor);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, btnColor);
        loginBtn.addActionListener(e -> login());

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(loginBtn, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        getContentPane().setBackground(backgroundColor);

        // 👇 This line was missing
        setVisible(true);
    }

    private void login() {
        String role = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("❌ Please enter both username and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (role.equals("Admin")) {
            AdminDAO dao = new AdminDAO();
            Admin admin = dao.validateAdmin(username, password);
            if (admin != null) {
                showMessage("✅ Welcome, " + admin.getUsername() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainFrame("Admin").setVisible(true);
            } else {
                showMessage("❌ Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            ReceptionistDAO dao = new ReceptionistDAO();
            Receptionist receptionist = dao.validateReceptionist(username, password);
            if (receptionist != null) {
                showMessage("✅ Welcome, " + receptionist.getUsername() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new ReceptionFrame("Receptionist").setVisible(true);
            } else {
                showMessage("❌ Invalid receptionist credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel getStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    private void styleField(JComponent field, Color bg, Color fg) {
        field.setBackground(bg);
        field.setForeground(fg);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        if (field instanceof JTextField) {
            ((JTextField) field).setCaretColor(fg);
        }
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showMessage(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
}
