package org.bhavesh.dao;

import org.bhavesh.model.Customer;
import org.bhavesh.model.Payment;
import org.bhavesh.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PaymentDAO {
    private Connection connection;
    private CustomerDAO customerDAO;

    public PaymentDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.customerDAO = new CustomerDAO();
    }

    public int addPayment(Payment payment) {
        String query = "INSERT INTO payments (customer_id, amount, payment_method, payment_status) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, payment.getCustomer().getCustomerId());
            statement.setDouble(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setString(4, payment.getPaymentStatus());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
                payment.setPaymentId(generatedId);
            }
        } catch (SQLException e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }
        return generatedId;
    }

    public ArrayList<Payment> getPaymentsByCustomer(int customerId) {
        ArrayList<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = customerDAO.getCustomerById(customerId);
                Payment payment = new Payment(
                        resultSet.getInt("payment_id"),
                        customer,
                        resultSet.getDouble("amount"),
                        resultSet.getString("payment_method"),
                        resultSet.getString("payment_status")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching payments: " + e.getMessage());
        }
        return payments;
    }
}