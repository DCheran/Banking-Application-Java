# Banking Application – Java Swing + SQLite

A desktop-based banking application developed as a Course End Project using
Java Swing, JDBC, SQLite, and Maven. The application supports customer
account management, deposits, withdrawals, balance validation, and persistent
transaction data storage.

## ✨ Features

- Create customer accounts
- Automatically generate unique account numbers
- Store customer details in SQLite
- View customer account details
- Deposit money
- Withdraw money
- Validate insufficient balance
- Record banking transactions
- Display loan information
- Input validation
- Persistent database storage
- Java Swing graphical user interface

## 🛠️ Technologies Used

- **Java 17+**
- **Java Swing**
- **JDBC**
- **SQLite**
- **Maven**
- **SQL**
- **Object-Oriented Programming**

## 🗂️ Project Structure

```text
Banking-Application-Java/
│
├── src/
│   └── main/
│       └── java/
│           ├── BankingAppSwing.java
│           │
│           ├── database/
│           │   └── DatabaseConnection.java
│           │
│           ├── dao/
│           │   └── CustomerDAO.java
│           │
│           └── model/
│               ├── Customer.java
│               └── Loan.java
│
├── database/
│   └── .gitkeep
│
├── screenshots/
│   ├── Account Creation.png
│   ├── Account Viewing.png
│   ├── Deposit.png
│   ├── Withdraw.png
│   └── Loans Details.png
│
├── pom.xml
├── README.md
└── .gitignore
