package org.bhavesh.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {
    private String role;

    public MainFrame(String role) {
        this.role = role;
        setTitle("Hotel Management - Admin Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color backgroundColor = new Color(46, 46, 46);
        Color textColor = new Color(211, 211, 211);
        Color buttonColor = new Color(0, 196, 180);
        Color menuGradientStart = new Color(60, 60, 60);
        Color menuGradientEnd = new Color(46, 46, 46);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, menuGradientStart, 0, getHeight(), menuGradientEnd);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(0, 0, 0, 50)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        menuPanel.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        int y = 0;

        JButton newCustomerButton = new JButton("New Customer Form");
        styleButton(newCustomerButton, buttonColor);
        newCustomerButton.setPreferredSize(new Dimension(350, 70));
        newCustomerButton.addActionListener(e -> {
            NewCustomerFrame newCustomerFrame = new NewCustomerFrame();
            newCustomerFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(newCustomerButton, gbc);

        JButton employeesInfoButton = new JButton("Employees Info");
        styleButton(employeesInfoButton, buttonColor);
        employeesInfoButton.setPreferredSize(new Dimension(350, 70));
        employeesInfoButton.addActionListener(e -> {
            EmployeesInfoFrame employeesInfoFrame = new EmployeesInfoFrame(role);
            employeesInfoFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(employeesInfoButton, gbc);

        JButton customerInfoButton = new JButton("Customer Info");
        styleButton(customerInfoButton, buttonColor);
        customerInfoButton.setPreferredSize(new Dimension(350, 70));
        customerInfoButton.addActionListener(e -> {
            CustomerInfoFrame customerInfoFrame = new CustomerInfoFrame();
            customerInfoFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(customerInfoButton, gbc);

        JButton checkOutButton = new JButton("Check Out");
        styleButton(checkOutButton, buttonColor);
        checkOutButton.setPreferredSize(new Dimension(350, 70));
        checkOutButton.addActionListener(e -> {
            CheckOutFrame checkOutFrame = new CheckOutFrame();
            checkOutFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(checkOutButton, gbc);

        JButton pickupServiceButton = new JButton("Pickup Service");
        styleButton(pickupServiceButton, buttonColor);
        pickupServiceButton.setPreferredSize(new Dimension(350, 70));
        pickupServiceButton.addActionListener(e -> {
            PickupServiceFrame pickupServiceFrame = new PickupServiceFrame();
            pickupServiceFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(pickupServiceButton, gbc);

        JButton roomManagementButton = new JButton("Room Management");
        styleButton(roomManagementButton, buttonColor);
        roomManagementButton.setPreferredSize(new Dimension(350, 70));
        roomManagementButton.addActionListener(e -> {
            RoomManagement roomManagementFrame = new RoomManagement();
            roomManagementFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(roomManagementButton, gbc);

        JButton pickupInfoButton = new JButton("Pickup Info");
        styleButton(pickupInfoButton, buttonColor);
        pickupInfoButton.setPreferredSize(new Dimension(350, 70));
        pickupInfoButton.addActionListener(e -> {
            PickupInfoFrame pickupInfoFrame = new PickupInfoFrame();
            pickupInfoFrame.setVisible(true);
        });
        gbc.gridy = y++;
        menuPanel.add(pickupInfoButton, gbc);

        mainPanel.add(menuPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setBackground(backgroundColor);
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true));

        try {
            File imageFile = new File("resources/images/hotel8.jpg");
            ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
            Image scaledImage = imageIcon.getImage().getScaledInstance(700, 650, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon, SwingConstants.CENTER);
            contentPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Image Not Found", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Roboto", Font.BOLD, 24));
            errorLabel.setForeground(textColor);
            contentPanel.add(errorLabel, BorderLayout.CENTER);
            System.out.println("Error loading image: " + e.getMessage());
        }

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(backgroundColor);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(backgroundColor);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, buttonColor);
        logoutButton.setPreferredSize(new Dimension(200, 60));
        logoutButton.setFont(new Font("Roboto", Font.BOLD, 18));
        logoutButton.addActionListener(e -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
        logoutPanel.add(logoutButton);

        footerPanel.add(logoutPanel, BorderLayout.CENTER);

        JLabel copyrightLabel = new JLabel("© 2025 Hotel Management System", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        copyrightLabel.setForeground(textColor);
        footerPanel.add(copyrightLabel, BorderLayout.SOUTH);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        getContentPane().setBackground(backgroundColor);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
