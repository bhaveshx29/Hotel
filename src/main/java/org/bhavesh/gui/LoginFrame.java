package org.bhavesh.gui;

import org.bhavesh.dao.AdminDAO;
import org.bhavesh.dao.ReceptionistDAO;
import org.bhavesh.model.Admin;
import org.bhavesh.model.Receptionist;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public LoginFrame() {
        setTitle("Hotel Management - Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Define the color scheme
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color inputFieldColor = new Color(74, 74, 74); // Medium gray (#4A4A4A)
        Color buttonColor = new Color(0, 196, 180); // Cyan (#00C4B4)
        Color formPanelColor = new Color(60, 60, 60); // Slightly lighter gray for form panel

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("HOTEL LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formPanelColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role
        JLabel roleLabel = new JLabel("Role:");
        styleLabel(roleLabel, textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(new String[]{"Admin", "Receptionist"});
        roleComboBox.setBackground(inputFieldColor);
        roleComboBox.setForeground(textColor);
        roleComboBox.setFont(new Font("Roboto", Font.PLAIN, 16));
        roleComboBox.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(roleComboBox, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        styleLabel(usernameLabel, textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        styleTextField(usernameField, inputFieldColor, textColor);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel, textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        styleTextField(passwordField, inputFieldColor, textColor);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, buttonColor);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void login() {
        String role = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showStyledMessageDialog("Please enter both username and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (role.equals("Admin")) {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.validateAdmin(username, password);
            if (admin != null) {
                showStyledMessageDialog("Welcome, " + admin.getUsername() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                MainFrame mainFrame = new MainFrame("Admin");
                mainFrame.setVisible(true);
            } else {
                showStyledMessageDialog("Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            ReceptionistDAO receptionistDAO = new ReceptionistDAO();
            Receptionist receptionist = receptionistDAO.validateReceptionist(username, password);
            if (receptionist != null) {
                showStyledMessageDialog("Welcome, " + receptionist.getUsername() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                ReceptionFrame receptionFrame = new ReceptionFrame("Receptionist");
                receptionFrame.setVisible(true);
            } else {
                showStyledMessageDialog("Invalid receptionist credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showStyledMessageDialog(String message, String title, int messageType) {
        // Create a custom panel for the message dialog
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(46, 46, 46));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(new Color(211, 211, 211));
        messageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Style the JOptionPane
        UIManager.put("OptionPane.background", new Color(46, 46, 46));
        UIManager.put("Panel.background", new Color(46, 46, 46));
        UIManager.put("Button.background", new Color(0, 196, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Roboto", Font.BOLD, 12));
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JOptionPane.showMessageDialog(this, messagePanel, title, messageType);
    }

    private void styleLabel(JLabel label, Color textColor) {
        label.setFont(new Font("Roboto", Font.PLAIN, 16));
        label.setForeground(textColor);
    }

    private void styleTextField(JTextField field, Color backgroundColor, Color textColor) {
        field.setFont(new Font("Roboto", Font.PLAIN, 16));
        field.setBackground(backgroundColor);
        field.setForeground(textColor);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setCaretColor(textColor);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
}