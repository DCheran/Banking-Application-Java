import dao.CustomerDAO;
import database.DatabaseConnection;
import model.Customer;
import model.Loan;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class BankingAppSwing extends JFrame {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final Random rand = new Random();

    public BankingAppSwing() {
        setTitle("Banking Application - Java Swing + SQLite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Create Account", createCreateAccountPanel());
        tabs.addTab("Customer Details", createCustomerDetailsPanel());
        tabs.addTab("Deposit", createDepositPanel());
        tabs.addTab("Withdraw", createWithdrawPanel());
        tabs.addTab("Loan Details", createLoanDetailsPanel());

        add(tabs);
    }

    private JPanel createCreateAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(25);

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(25);

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(15);

        JLabel openingLabel = new JLabel("Opening Balance (₹):");
        JTextField openingField = new JTextField(10);

        JButton createButton = new JButton("Create Account");

        addField(form, gbc, nameLabel, nameField, 0);
        addField(form, gbc, addressLabel, addressField, 1);
        addField(form, gbc, phoneLabel, phoneField, 2);
        addField(form, gbc, openingLabel, openingField, 3);

        gbc.gridx = 1;
        gbc.gridy = 4;
        form.add(createButton, gbc);

        panel.add(form, BorderLayout.NORTH);

        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            String openingText = openingField.getText().trim();

            if (name.isEmpty() || address.isEmpty() ||
                    phone.isEmpty() || openingText.isEmpty()) {
                showError("Please fill all fields.");
                return;
            }

            double openingBalance;

            try {
                openingBalance = Double.parseDouble(openingText);

                if (openingBalance < 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException ex) {
                showError("Invalid opening balance.");
                return;
            }

            String accountNumber = generateUniqueAccountNumber();

            Customer customer = new Customer(
                    name,
                    address,
                    phone,
                    accountNumber,
                    openingBalance
            );

            try {
                customerDAO.createCustomer(customer);

                JOptionPane.showMessageDialog(
                        this,
                        "Account Created Successfully!\n" +
                                "Account Number: " + accountNumber,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                nameField.setText("");
                addressField.setText("");
                phoneField.setText("");
                openingField.setText("");

            } catch (RuntimeException ex) {
                showError("Could not create account.\n" + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createCustomerDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();

        top.add(new JLabel("Account Number:"));

        JTextField accountField = new JTextField(12);
        top.add(accountField);

        JButton viewButton = new JButton("View Details");
        top.add(viewButton);

        JTextArea output = new JTextArea(10, 45);
        output.setEditable(false);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(output), BorderLayout.CENTER);

        viewButton.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();

            if (accountNumber.isEmpty()) {
                showError("Enter account number.");
                return;
            }

            Customer customer = customerDAO.findByAccountNumber(accountNumber);

            if (customer == null) {
                showError("Account not found.");
                output.setText("");
            } else {
                output.setText(customer.displayDetailsString());
            }
        });

        return panel;
    }

    private JPanel createDepositPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel accountLabel = new JLabel("Account Number:");
        JTextField accountField = new JTextField(12);

        JLabel amountLabel = new JLabel("Amount (₹):");
        JTextField amountField = new JTextField(10);

        JButton depositButton = new JButton("Deposit");

        addField(panel, gbc, accountLabel, accountField, 0);
        addField(panel, gbc, amountLabel, amountField, 1);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(depositButton, gbc);

        depositButton.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();
            String amountText = amountField.getText().trim();

            if (accountNumber.isEmpty() || amountText.isEmpty()) {
                showError("Enter account and amount.");
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                showError("Invalid amount.");
                return;
            }

            if (amount <= 0) {
                showError("Deposit amount must be greater than zero.");
                return;
            }

            Customer customer =
                    customerDAO.findByAccountNumber(accountNumber);

            if (customer == null) {
                showError("Account not found.");
                return;
            }

            double newBalance = customer.getBalance() + amount;

            customerDAO.updateBalance(
                    accountNumber,
                    newBalance,
                    "DEPOSIT",
                    amount
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Deposit successful.\nNew Balance: ₹" +
                            String.format("%.2f", newBalance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            accountField.setText("");
            amountField.setText("");
        });

        return panel;
    }

    private JPanel createWithdrawPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel accountLabel = new JLabel("Account Number:");
        JTextField accountField = new JTextField(12);

        JLabel amountLabel = new JLabel("Amount (₹):");
        JTextField amountField = new JTextField(10);

        JButton withdrawButton = new JButton("Withdraw");

        addField(panel, gbc, accountLabel, accountField, 0);
        addField(panel, gbc, amountLabel, amountField, 1);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(withdrawButton, gbc);

        withdrawButton.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();
            String amountText = amountField.getText().trim();

            if (accountNumber.isEmpty() || amountText.isEmpty()) {
                showError("Enter account and amount.");
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                showError("Invalid amount.");
                return;
            }

            if (amount <= 0) {
                showError("Withdrawal amount must be greater than zero.");
                return;
            }

            Customer customer =
                    customerDAO.findByAccountNumber(accountNumber);

            if (customer == null) {
                showError("Account not found.");
                return;
            }

            if (amount > customer.getBalance()) {
                showError("Insufficient balance.");
                return;
            }

            double newBalance = customer.getBalance() - amount;

            customerDAO.updateBalance(
                    accountNumber,
                    newBalance,
                    "WITHDRAW",
                    amount
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Withdrawal successful.\nNew Balance: ₹" +
                            String.format("%.2f", newBalance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            accountField.setText("");
            amountField.setText("");
        });

        return panel;
    }

    private JPanel createLoanDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        panel.add(new JScrollPane(text), BorderLayout.CENTER);

        Loan[] loans = {
                new Loan("Home Loan", 8.5,
                        "Minimum income ₹25,000/month"),
                new Loan("Car Loan", 9.0,
                        "Valid driving license and income proof"),
                new Loan("Education Loan", 7.5,
                        "Student admission proof required"),
                new Loan("Personal Loan", 10.5,
                        "Stable income and credit score > 700")
        };

        StringBuilder builder = new StringBuilder();
        builder.append("--- Loan Details ---\n\n");

        for (Loan loan : loans) {
            builder.append("Type: ")
                    .append(loan.getType())
                    .append("\n");

            builder.append("Interest Rate: ")
                    .append(loan.getInterestRate())
                    .append("%\n");

            builder.append("Eligibility: ")
                    .append(loan.getEligibility())
                    .append("\n");

            builder.append("-----------------------------------------\n");
        }

        text.setText(builder.toString());

        return panel;
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;

        do {
            int number = 1000 + rand.nextInt(9000);
            accountNumber = "AC" + number;
        } while (customerDAO.findByAccountNumber(accountNumber) != null);

        return accountNumber;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            JLabel label,
            JTextField field,
            int row) {

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        DatabaseConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            BankingAppSwing app = new BankingAppSwing();
            app.setVisible(true);
        });
    }
}
