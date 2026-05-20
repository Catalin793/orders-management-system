package org.tucn.pt.dataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestioneaza conexiunea la baza de date MySQL
 */
public class ConnectionFactory {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/orders_management";
    private static final String USER = "root";
    private static final String PASS = "1234";

    private static ConnectionFactory conn = new ConnectionFactory();

    /**
     * Constructor privat pentru a preveni instantierea externa.
     * Va asigura incarcarea driverului MySQL la initiere.
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Eroare la incarcarea driverului MySQL: " + e.getMessage());
        }
    }

    /**
     * Returneaza o conexiune activa la baza de date folosind credentialele statice declarate.
     *
     * @return un obiect Connection catre serverul MySQL
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Eroare la conectarea la baza de date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inchide un obiect de tip Connection, eliberand resursele.
     *
     * @param connection conexiunea care va fi inchisa
     */
    public static void close(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println("Eroare la inchiderea conexiunii: " + e.getMessage());
        }
    }

    /**
     * Inchide o interogare de tip Statement, eliberand resursele asociate.
     *
     * @param statement interogarea executata ce va fi inchisa
     */
    public static void close(Statement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            System.out.println("Eroare la inchiderea statement-ului: " + e.getMessage());
        }
    }

    /**
     * Inchide un ResultSet, eliberand memoria alocata acestuia.
     *
     * @param resultSet setul de date primit dupa o interogare
     */
    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            System.out.println("Eroare la inchiderea ResultSet-ului: " + e.getMessage());
        }
    }
}