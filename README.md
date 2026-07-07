# Orders Management System

A Java application for managing clients, products, and orders using a layered architecture and a MySQL database.

## Overview

This project was developed to simulate a basic order management system.

The application allows users to:
- Manage clients
- Manage products
- Create orders
- Store and retrieve data from a MySQL database

The project follows a layered architecture that separates:
- Presentation Layer
- Business Logic Layer
- Data Access Layer (DAO)

## Technologies

- Java
- JDBC
- MySQL
- Swing
- Object-Oriented Programming

## Architecture

The application is structured using multiple layers:

### Presentation Layer
Provides the graphical user interface and user interaction.

### Business Logic Layer
Handles validation and business rules.

### Data Access Layer
Responsible for database communication using JDBC.

## Features

### Client Management
- Add clients
- Edit client information
- Delete clients
- View all clients

### Product Management
- Add products
- Update stock
- Delete products
- View available products

### Order Management
- Create orders
- Validate stock availability
- Update inventory automatically
- Generate order information

## Database

The application uses a MySQL database containing:

- Clients
- Products
- Orders

Relationships between entities are handled through foreign keys and database constraints.

## Learning Outcomes

This project helped me gain practical experience with:

- Object-Oriented Programming
- Layered Software Architecture
- JDBC Database Connectivity
- CRUD Operations
- SQL Queries
- Java GUI Development

## Build & Run

1. Create a MySQL database.
2. Configure the database connection.
3. Compile and run the application.

```bash
javac *.java
java Main
```

## Project Context

Developed as part of the Object-Oriented Programming course at the Technical University of Cluj-Napoca.