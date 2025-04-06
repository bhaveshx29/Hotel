package org.bhavesh.gui;
import org.bhavesh.dao.CustomerDAO;
import org.bhavesh.dao.BookingDAO;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import javax.swing.table.JTableHeader;
import org.bhavesh.model.Customer;



public class CustomerDashboardFrame extends JFrame {
    private int currentCustomerId;
    private CardLayout cardLayout;
    private JPanel cardPanel;



    public CustomerDashboardFrame(int customerId) {
        this.currentCustomerId = customerId;
        setTitle("Mau Hotel - Customer Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initTopBar();
        initSidebar();
        initCardPanel();

        setVisible(true);

    }

    private void initTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(30, 30, 30));
        topBar.setPreferredSize(new Dimension(1000, 60));

        JLabel hotelName = new JLabel("🏨 Mau Hotel");
        hotelName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        hotelName.setForeground(Color.CYAN);
        hotelName.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));




        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(topBar.getBackground());



        topBar.add(rightPanel, BorderLayout.EAST);

        topBar.add(hotelName, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
    }

    private void initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 45, 45));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        // List of buttons
        String[] buttons = {"Home", "Search Rooms", "Book Room", "My Bookings", "Request Pickup", "Feedback"};

        // Add each button to the sidebar
        for (String btnText : buttons) {
            JButton btn = new JButton(btnText);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setFocusPainted(false);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.CYAN);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            btn.addActionListener(e -> cardLayout.show(cardPanel, btnText.toLowerCase().replaceAll(" ", "")));

            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);
        }

        // Adding the Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(180, 40));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(Color.DARK_GRAY);
        logoutBtn.setForeground(Color.CYAN);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));


            logoutBtn.addActionListener(e -> {
                dispose();
                new WelcomeFrame().setVisible(true); // Open WelcomeFrame after logout
            });


            // Add some space before the logout button
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(logoutBtn);

        // Add the sidebar to the main layout
        add(sidebar, BorderLayout.WEST);
    }


    private void initCardPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(60, 60, 60));
        cardPanel.add(getCenteredLabel("🏠 Welcome to Mau Hotel!"), "home");
        cardPanel.add(createSearchRoomsPanel(), "searchrooms");
        cardPanel.add(createBookRoomPanel(), "bookroom");
        cardPanel.add(createMyBookingsPanel(), "mybookings");
        cardPanel.add(createRequestPickupPanel(), "requestpickup");
        cardPanel.add(createFeedbackPanel(), "feedback");


        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 60));

        String[] columnNames = {"Room ID", "Room Type", "Price per Night", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(100, 100, 100));
        table.setForeground(Color.CYAN);
        table.setBackground(new Color(70, 70, 70));
        table.setSelectionBackground(new Color(100, 100, 100));
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.CYAN);
        table.getTableHeader().setBackground(new Color(40, 40, 40));

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                label.setOpaque(true);
                label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

                if ("Yes".equals(value)) {
                    label.setText("✅");
                    label.setForeground(new Color(0, 255, 0));
                } else {
                    label.setText("❌");
                    label.setForeground(new Color(255, 0, 0));
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM rooms");

            while (rs.next()) {
                int id = rs.getInt("room_id");
                String type = rs.getString("room_type");
                double price = rs.getDouble("price_per_night");
                String available = rs.getInt("availability") == 1 ? "Yes" : "No";

                model.addRow(new Object[]{id, type, price, available});
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Could not load rooms!", "DB Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JPanel createBookRoomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(60, 60, 60));
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // UI components
        JTextField nameField = createStyledField();
        JTextField emailField = createStyledField();
        JTextField phoneField = createStyledField();

        // (Optional) Auto-fill user info
        Customer currentCustomer = new CustomerDAO().getCustomerById(currentCustomerId);
        if (currentCustomer != null) {
            nameField.setText(currentCustomer.getName());
            emailField.setText(currentCustomer.getEmail());
            phoneField.setText(currentCustomer.getPhone());
        }

        JComboBox<String> roomDropdown = new JComboBox<>();
        Map<String, Integer> roomMap = new HashMap<>();

        // Load available rooms
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT room_id, room_type FROM rooms")) {

            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                String type = rs.getString("room_type");
                String label = type + " (ID: " + roomId + ")";
                roomDropdown.addItem(label);
                roomMap.put(label, roomId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDateChooser checkInPicker = new JDateChooser();
        JDateChooser checkOutPicker = new JDateChooser();
        checkInPicker.setDateFormatString("yyyy-MM-dd");
        checkOutPicker.setDateFormatString("yyyy-MM-dd");

        JButton bookBtn = new JButton("Book Now");
        bookBtn.setBackground(Color.DARK_GRAY);
        bookBtn.setForeground(Color.CYAN);
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Layout
        panel.add(getFormLabel("👤 Name:")); panel.add(nameField);
        panel.add(getFormLabel("📧 Email:")); panel.add(emailField);
        panel.add(getFormLabel("📞 Phone:")); panel.add(phoneField);
        panel.add(getFormLabel("🏨 Room:")); panel.add(roomDropdown);
        panel.add(getFormLabel("📅 Check-in Date:")); panel.add(checkInPicker);
        panel.add(getFormLabel("📅 Check-out Date:")); panel.add(checkOutPicker);
        panel.add(new JLabel()); panel.add(bookBtn);

        // Action Listener
        bookBtn.addActionListener(e -> {
            try {
                if (!showPaymentDialog(nameField.getText(), 1000)) {
                    JOptionPane.showMessageDialog(this, "Payment Cancelled.");
                    return;
                }
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "")) {
                    String insertPayment = "INSERT INTO payments (customer_id, amount, payment_method, payment_status) VALUES (?, ?, ?, ?)";
                    PreparedStatement payStmt = conn.prepareStatement(insertPayment);
                    payStmt.setInt(1, currentCustomerId);
                    payStmt.setDouble(2, 1000); // Fixed for now
                    payStmt.setString(3, "Card");
                    payStmt.setString(4, "Completed");
                    payStmt.executeUpdate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "❌ Payment recorded failed: " + ex.getMessage());
                }
                int roomId = roomMap.get((String) roomDropdown.getSelectedItem());
                String checkIn = new SimpleDateFormat("yyyy-MM-dd").format(checkInPicker.getDate());
                String checkOut = new SimpleDateFormat("yyyy-MM-dd").format(checkOutPicker.getDate());

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "");
                     PreparedStatement ps = conn.prepareStatement(
                             "INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)")) {

                    ps.setInt(1, currentCustomerId);
                    ps.setInt(2, roomId);
                    ps.setString(3, checkIn);
                    ps.setString(4, checkOut);
                    ps.setString(5, "Confirmed");

                    ps.executeUpdate();
                }

                sendReceiptEmail(emailField.getText(), nameField.getText(), roomId);
                JOptionPane.showMessageDialog(this, "✅ Booking Confirmed! Receipt sent to email.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Booking failed: " + ex.getMessage());
            }
        });

        return panel;
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



    private boolean showPaymentDialog(String customerName, double amount) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        fieldsPanel.setBackground(new Color(30, 30, 30));

        JTextField cardHolder = createStyledField();
        JTextField cardNumber = createStyledField();
        JTextField expiry = createStyledField();
        JTextField cvv = createStyledField();

        fieldsPanel.add(getFormLabel("💳 Card Holder:")); fieldsPanel.add(cardHolder);
        fieldsPanel.add(getFormLabel("🔢 Card Number:")); fieldsPanel.add(cardNumber);
        fieldsPanel.add(getFormLabel("📅 Expiry (MM/YY):")); fieldsPanel.add(expiry);
        fieldsPanel.add(getFormLabel("🔐 CVV:")); fieldsPanel.add(cvv);

        panel.add(fieldsPanel, BorderLayout.CENTER);

        // Styling the JOptionPane buttons
        UIManager.put("OptionPane.background", new Color(30, 30, 30));
        UIManager.put("Panel.background", new Color(30, 30, 30));
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.CYAN);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);


        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    null, panel,
                    "💳 Secure Payment - Rs " + amount,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return false;
            }

            // Validation logic
            String holder = cardHolder.getText().trim();
            String number = cardNumber.getText().trim();
            String exp = expiry.getText().trim();
            String cvvText = cvv.getText().trim();

            if (holder.isEmpty() || number.isEmpty() || exp.isEmpty() || cvvText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "❌ Please fill in all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!number.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(null, "❌ Card Number must be 16 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!exp.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                JOptionPane.showMessageDialog(null, "❌ Expiry must be in MM/YY format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!cvvText.matches("\\d{3}")) {
                JOptionPane.showMessageDialog(null, "❌ CVV must be 3 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // All checks passed
            return true;
        }
    }

    private JLabel getFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private void sendReceiptEmail(String toEmail, String name, int roomId) {
        final String fromEmail = "MauHotel12@gmail.com";
        final String password = "fgcl shzm gjdr ihdh";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("🏨 Mau Hotel - Booking Receipt");

            String content = "<div style='font-family:Segoe UI, sans-serif; font-size:14px; color:#333;'>"
                    + "<p>Dear Mr. " + name + ",</p>"
                    + "<p>Your reservation for <strong>Room #" + roomId + "</strong> at <strong>Mau Hotel</strong> has been confirmed. "
                    + "We appreciate your booking and look forward to hosting you.</p>"
                    + "<p>Should you need any assistance, please don’t hesitate to contact us.</p>"
                    + "<br>"
                    + "<p>Best regards,<br><strong>Mau Hotel</strong></p>"
                    + "</div>";
            message.setContent(content, "text/html");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(46, 46, 46));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table setup
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Booking ID", "Room ID", "Check-In", "Check-Out", "Status"}, 0);
        table.setModel(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Color.CYAN);
        table.setBackground(new Color(74, 74, 74));
        table.setRowHeight(30);
        table.setGridColor(Color.GRAY);
        table.setSelectionBackground(new Color(0, 196, 180));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        // Table header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(Color.CYAN);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Cancel booking button
        JButton cancelBtn = new JButton("Cancel Selected Booking");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelBtn.setBackground(Color.decode("#CC0000"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelBtn.setBackground(Color.RED);
            }
        });

        cancelBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this booking?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    int bookingId = (int) table.getValueAt(selectedRow, 0);
                    cancelBooking(bookingId);
                    model.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(cancelBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Load Data from DB
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "")) {
            System.out.println("Current Customer ID: " + currentCustomerId);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM reservations WHERE customer_id = ?");
            ps.setInt(1, currentCustomerId); // make sure this is defined in your class
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("reservation_id"),
                        rs.getInt("room_id"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return panel;
    }


    private void cancelBooking(int bookingId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM reservations WHERE reservation_id = ?");
            ps.setInt(1, bookingId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Booking cancelled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error cancelling booking: " + e.getMessage());
        }
    }



    private JPanel createRequestPickupPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField locationField = new JTextField();
        JTextField dateField = new JTextField("YYYY-MM-DD HH:MM:SS"); // Placeholder
        JTextField vehicleField = new JTextField();

        styleField(locationField);
        styleField(dateField);
        styleField(vehicleField);

        panel.add(createLabel("📍 Pickup Location:")); panel.add(locationField);
        panel.add(createLabel("📅 Pickup Date/Time:")); panel.add(dateField);
        panel.add(createLabel("🚗 Vehicle Info:")); panel.add(vehicleField);

        JButton submitBtn = new JButton("Request Pickup");
        styleButton(submitBtn);

        submitBtn.addActionListener(e -> {
            String location = locationField.getText().trim();
            String date = dateField.getText().trim();
            String vehicle = vehicleField.getText().trim();

            if (location.isEmpty() || date.isEmpty() || vehicle.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "❌ All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_booking_db", "root", "")) {
                String sql = "INSERT INTO pickup_services (customer_id, pickup_location, pickup_date, vehicle_info) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, currentCustomerId);
                ps.setString(2, location);
                ps.setString(3, date);
                ps.setString(4, vehicle);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(panel, "✅ Pickup requested successfully!");
                locationField.setText("");
                dateField.setText("");
                vehicleField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "❌ Failed to request pickup.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel()); // empty cell
        panel.add(submitBtn);

        return panel;
    }

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(33, 33, 33)); // Deep dark background
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel titleLabel = new JLabel("📝 Leave Your Feedback");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea feedbackArea = new JTextArea(8, 40);
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        feedbackArea.setBackground(new Color(50, 50, 50)); // Dark input box
        feedbackArea.setForeground(Color.WHITE);
        feedbackArea.setCaretColor(Color.CYAN);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.CYAN, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton submitButton = new JButton("Submit Feedback");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setBackground(Color.CYAN);
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Subtle hover effect
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(0, 255, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(Color.CYAN);
            }
        });

        submitButton.addActionListener(e -> {
            String feedback = feedbackArea.getText().trim();
            if (!feedback.isEmpty()) {
                CustomerDAO.submitFeedback(currentCustomerId, feedback);
                JOptionPane.showMessageDialog(panel, "Thank you for your feedback!");
                feedbackArea.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Please write something before submitting!");
            }
        });

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(submitButton);

        return panel;
    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 200, 255)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 196, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    private JPanel getCenteredLabel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(60, 60, 60));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }


}
