# Banking Application – Java Swing + SQLite

A Course End Project for the Object Oriented Programming Laboratory.

## Features

- Create customer accounts
- Generate unique account numbers
- Store customer details in SQLite
- View customer details after restarting the application
- Deposit money
- Withdraw money
- Validate insufficient balance
- Record deposits and withdrawals in a transaction table
- Display loan information
- Java Swing graphical interface

## Technologies

- Java 17+
- Java Swing
- JDBC
- SQLite
- Maven
- Object-Oriented Programming
- SQL

The project uses the Xerial SQLite JDBC driver through Maven.

## Project Structure

```text
Banking-Application-Java/
├── src/
│   └── main/
│       └── java/
│           ├── BankingAppSwing.java
│           ├── database/
│           │   └── DatabaseConnection.java
│           ├── dao/
│           │   └── CustomerDAO.java
│           └── model/
│               ├── Customer.java
│               └── Loan.java
├── database/
│   └── banking.db          # created automatically when the app runs
├── screenshots/
├── pom.xml
├── README.md
└── .gitignore
```

## Database

The application creates the SQLite database automatically on first launch.

### customers

Stores:

- name
- address
- phone
- account number
- balance

### transactions

Stores:

- account number
- transaction type
- amount
- transaction date

## Run with Maven

Make sure Java and Maven are installed.

From the project root:

```bash
mvn clean compile
mvn exec:java
```

The SQLite JDBC dependency is downloaded automatically by Maven.

## Important

The database file is generated locally and is not intended to contain real banking information. This is an academic banking simulation, not a production banking system.

Do not use real personal, financial, account, or payment information.

## Learning Outcomes

This project demonstrates:

- Classes and objects
- Encapsulation
- JDBC
- SQL CRUD operations
- Prepared statements
- Database transactions
- DAO pattern
- Exception handling
- Java Swing event handling
- Input validation
- Persistent data storage
