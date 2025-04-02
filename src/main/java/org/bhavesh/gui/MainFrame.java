package org.bhavesh.gui;

import org.bhavesh.model.User;
import javax.swing.*;

public class MainFrame extends JFrame {
    private User currentUser;
    
    public MainFrame(User loggedInUser) {
        this.currentUser = loggedInUser;
        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("Hotel Management System - " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Create main tabbed interface
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Add Dashboard panel for all users
        tabbedPane.addTab("Dashboard", new DashboardPanel(currentUser));
        
        // Add role-specific panels
        if (currentUser.getRole().equals("ADMIN")) {
            tabbedPane.addTab("Customers", new CustomerPanel());
           
        } else if (currentUser.getRole().equals("RECEPTIONIST")) {
            tabbedPane.addTab("Customers", new CustomerPanel());
           
        }
        
        add(tabbedPane);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginScreen().setVisible(true);
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
            this,
            "Hotel Management System\nVersion 1.0\n© 2023 Bhavesh Org",
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Dashboard Panel
    class DashboardPanel extends JPanel {
        public DashboardPanel(User user) {
            setLayout(new BorderLayout());
            
            // Welcome panel
            JPanel welcomePanel = new JPanel();
            welcomePanel.add(new JLabel("Welcome, " + user.getUsername() + " (" + user.getRole() + ")"));
            add(welcomePanel, BorderLayout.NORTH);
            
            // Statistics panel
            JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            statsPanel.add(createStatCard("Total Rooms", "50"));
            statsPanel.add(createStatCard("Occupied", "32"));
            statsPanel.add(createStatCard("Available", "18"));
            statsPanel.add(createStatCard("Active Employees", "8"));
            
            add(statsPanel, BorderLayout.CENTER);
        }
        
        private JPanel createStatCard(String title, String value) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            
            card.add(titleLabel, BorderLayout.NORTH);
            card.add(valueLabel, BorderLayout.CENTER);
            
            return card;
        }
    }
}