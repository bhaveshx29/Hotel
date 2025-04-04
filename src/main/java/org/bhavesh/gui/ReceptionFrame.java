package org.bhavesh.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ReceptionFrame extends JFrame {
    private String role;

    public ReceptionFrame(String role) {
        this.role = role;
        setTitle("Hotel Management - Reception");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Define the color scheme
        Color backgroundColor = new Color(46, 46, 46); // Dark gray (#2E2E2E)
        Color textColor = new Color(211, 211, 211); // Light gray (#D3D3D3)
        Color buttonColor = new Color(0, 196, 180); // Cyan (#00C4B4)

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("RECEPTION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Menu Panel (Left Side)
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(backgroundColor);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setPreferredSize(new Dimension(250, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Padding between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Receptionist functionalities
        JButton newCustomerButton = new JButton("New Customer Form");
        styleButton(newCustomerButton, buttonColor);
        newCustomerButton.addActionListener(e -> {
            NewCustomerFrame newCustomerFrame = new NewCustomerFrame();
            newCustomerFrame.setVisible(true);
        });
        gbc.gridy = 0;
        menuPanel.add(newCustomerButton, gbc);

        JButton customerInfoButton = new JButton("Customer Info");
        styleButton(customerInfoButton, buttonColor);
        customerInfoButton.addActionListener(e -> {
            CustomerInfoFrame customerInfoFrame = new CustomerInfoFrame();
            customerInfoFrame.setVisible(true);
        });
        gbc.gridy = 1;
        menuPanel.add(customerInfoButton, gbc);

        JButton checkOutButton = new JButton("Check Out");
        styleButton(checkOutButton, buttonColor);
        checkOutButton.addActionListener(e -> {
            CheckOutFrame checkOutFrame = new CheckOutFrame();
            checkOutFrame.setVisible(true);
        });
        gbc.gridy = 2;
        menuPanel.add(checkOutButton, gbc);

        JButton pickupServiceButton = new JButton("Pickup Service");
        styleButton(pickupServiceButton, buttonColor);
        pickupServiceButton.addActionListener(e -> {
            PickupServiceFrame pickupServiceFrame = new PickupServiceFrame();
            pickupServiceFrame.setVisible(true);
        });
        gbc.gridy = 3;
        menuPanel.add(pickupServiceButton, gbc);

        JButton pickupInfoButton = new JButton("Pickup Info");
        styleButton(pickupInfoButton, buttonColor);
        pickupInfoButton.addActionListener(e -> {
            PickupInfoFrame pickupInfoFrame = new PickupInfoFrame();
            pickupInfoFrame.setVisible(true);
        });
        gbc.gridy = 4;
        menuPanel.add(pickupInfoButton, gbc);

        mainPanel.add(menuPanel, BorderLayout.WEST);

        // Image Panel (Right Side)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(backgroundColor);

        try {
            File imageFile = new File("resources/images/hotel8.jpg");
            ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
            Image scaledImage = imageIcon.getImage().getScaledInstance(550, 500, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon, SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Image Not Found", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Roboto", Font.BOLD, 24));
            errorLabel.setForeground(textColor);
            imagePanel.add(errorLabel, BorderLayout.CENTER);
            System.out.println("Error loading image: " + e.getMessage());
        }

        mainPanel.add(imagePanel, BorderLayout.CENTER);

        // Bottom Panel (Logout Button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, buttonColor);
        logoutButton.addActionListener(e -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
        bottomPanel.add(logoutButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Set the frame background
        getContentPane().setBackground(backgroundColor);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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