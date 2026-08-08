package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:database/banking.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    address TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    account_number TEXT NOT NULL UNIQUE,
                    balance REAL NOT NULL DEFAULT 0
                )
                """;

        String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_number TEXT NOT NULL,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (account_number) REFERENCES customers(account_number)
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createCustomersTable);
            statement.execute(createTransactionsTable);

        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize database.", e);
        }
    }
}
