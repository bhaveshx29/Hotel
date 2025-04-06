package org.bhavesh.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Mau Hotel Portal");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("🏨 Mau Hotel Portal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.CYAN);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Buttons
        JButton customerBtn = createStyledBtn("Customer Login");
        JButton adminBtn = createStyledBtn("Admin Login");

        customerBtn.addActionListener(e -> {
            dispose(); // Close welcome screen
            new CustomerLoginFrame(); // Open customer login
        });

        adminBtn.addActionListener(e -> {
            dispose(); // Open your already-styled LoginFrame
            new LoginFrame();
        });

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        btnPanel.setBackground(new Color(30, 30, 30));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));
        btnPanel.add(customerBtn);
        btnPanel.add(adminBtn);

        add(btnPanel, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(30, 30, 30));
        setVisible(true);
    }

    private JButton createStyledBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.CYAN);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeFrame::new);
    }
}
