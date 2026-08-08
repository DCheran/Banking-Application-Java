package dao;

import database.DatabaseConnection;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public String createCustomer(Customer customer) {
        String sql = """
                INSERT INTO customers
                (name, address, phone, account_number, balance)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getAccountNumber());
            statement.setDouble(5, customer.getBalance());

            statement.executeUpdate();

            if (customer.getBalance() > 0) {
                recordTransaction(
                        connection,
                        customer.getAccountNumber(),
                        "OPENING_BALANCE",
                        customer.getBalance()
                );
            }

            return customer.getAccountNumber();

        } catch (SQLException e) {
            throw new RuntimeException("Could not create account.", e);
        }
    }

    public Customer findByAccountNumber(String accountNumber) {
        String sql = """
                SELECT name, address, phone, account_number, balance
                FROM customers
                WHERE account_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Customer(
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("phone"),
                            result.getString("account_number"),
                            result.getDouble("balance")
                    );
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Could not fetch account.", e);
        }
    }

    public List<Customer> findAll() {
        String sql = """
                SELECT name, address, phone, account_number, balance
                FROM customers
                ORDER BY id
                """;

        List<Customer> customers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                customers.add(new Customer(
                        result.getString("name"),
                        result.getString("address"),
                        result.getString("phone"),
                        result.getString("account_number"),
                        result.getDouble("balance")
                ));
            }

            return customers;

        } catch (SQLException e) {
            throw new RuntimeException("Could not fetch customers.", e);
        }
    }

    public boolean updateBalance(String accountNumber, double newBalance,
                                 String transactionType, double amount) {
        String updateSql = """
                UPDATE customers
                SET balance = ?
                WHERE account_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                update.setDouble(1, newBalance);
                update.setString(2, accountNumber);

                int rows = update.executeUpdate();

                if (rows != 1) {
                    connection.rollback();
                    return false;
                }

                recordTransaction(
                        connection,
                        accountNumber,
                        transactionType,
                        amount
                );

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not update balance.", e);
        }
    }

    private void recordTransaction(Connection connection,
                                   String accountNumber,
                                   String type,
                                   double amount) throws SQLException {

        String sql = """
                INSERT INTO transactions
                (account_number, type, amount)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setString(2, type);
            statement.setDouble(3, amount);
            statement.executeUpdate();
        }
    }
}
