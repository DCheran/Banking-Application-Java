package model;

public class Customer {

    private final String name;
    private final String address;
    private final String phone;
    private final String accountNumber;
    private double balance;

    public Customer(String name, String address, String phone,
                    String accountNumber, double balance) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String displayDetailsString() {
        return String.format(
                "--- Customer Details ---\n" +
                "Name: %s\n" +
                "Address: %s\n" +
                "Phone: %s\n" +
                "Account Number: %s\n" +
                "Balance: ₹%.2f%n",
                name, address, phone, accountNumber, balance
        );
    }
}
