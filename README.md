# Scalabank

Scalabank is a Scala-based banking library developed as part of a Programming and Development Paradigms course. The project emphasizes clean code, modular design and functional programming principles. While the library offers robust backend functionality, the graphical user interface (GUI) is minimal and serves primarily for demonstration purposes.

## Features

- **Bank Account Management** -> create and manage multiple bank accounts
- **Transactions** -> perform deposits, withdrawals and transfers between accounts
- **Interest Calculation** -> apply interest rates to accounts over time
- **Transaction History** -> maintain a log of all account activities.
- **Concurrency Support** -> handle multiple transactions simultaneously with thread safety.

## Prerequisites

Ensure you have the following installed:

- [Scala 3](https://www.scala-lang.org/download/)
- [Java Development Kit (JDK) 11 or higher](https://adoptopenjdk.net/)
- [Gradle](https://gradle.org/install/)

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/FabioNotaro2001/scalabank.git
   cd scalabank

Build the project using Gradle:
./gradlew build

To run the application:
./gradlew run

    This will start the application with the default settings.

Usage

After starting the application, you can interact with it through the console. The available commands include:

    createAccount <name>: Creates a new account with the specified name.

    deposit <accountId> <amount>: Deposits the specified amount into the given account.

    withdraw <accountId> <amount>: Withdraws the specified amount from the given account.

    transfer <fromAccountId> <toAccountId> <amount>: Transfers the specified amount from one account to another.

    applyInterest <accountId>: Applies the current interest rate to the specified account.

    exit: Exits the application.

Example Session
> createAccount Alice
Account created: Alice

> deposit 1 1000
Deposited 1000 to account 1

> withdraw 1 200
Withdrew 200 from account 1

> transfer 1 2 300
Transferred 300 from account 1 to account 2

> applyInterest 1
Applied interest to account 1

> exit
Exiting application...

Contributing

Contributions are welcome! Please fork the repository, create a new branch, and submit a pull request with your proposed changes.
License

This project is licensed under the MIT License - see the LICENSE file for details.
Acknowledgements

    Scala - The programming language used for development.

    Gradle - The build automation tool.

    Java - The programming language used for backend services.
