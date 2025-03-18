package org.bhavesh.gui;

import javax.swing.*;
import org.bhavesh.gui.RoomPanel;
import org.bhavesh.gui.ReservationPanel;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Hotel Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a tabbed pane to organize the panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add the Customer Panel
        CustomerPanel customerPanel = new CustomerPanel();
        tabbedPane.addTab("Customers", customerPanel);

        // Add the Room Panel
        RoomPanel roomPanel = new RoomPanel();
        tabbedPane.addTab("Rooms", roomPanel);

        // Add the Reservation Panel
        ReservationPanel reservationPanel = new ReservationPanel();
        tabbedPane.addTab("Reservations", reservationPanel);

        // Add the tabbed pane to the frame
        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}