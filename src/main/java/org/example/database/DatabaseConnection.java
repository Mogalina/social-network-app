package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class responsible for managing database connection.
 * This class provides a way to obtain a single instance of database connection across application for common usage.
 */
public class DatabaseConnection {

    // Singleton instance of databse connection
    private static DatabaseConnection instance;

    // Actual database connection object
    private Connection connection;

    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();

    // Database connection credentials
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    /**
     * Constructs a new DatabaseConnection instance if it does not exist.
     * This constructor prevents instantiation from outside of class.
     */
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("[INFO] Database connection established");
        } catch (SQLException e) {
            System.out.println("[ERROR] Database connection failed. " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Returns the singleton instance of DatabaseConnection if it already exists, otherwise creates a new
     * DatabaseConnection instance and returns it.
     *
     * @return the singleton instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns the established connection to the database.
     *
     * @return the database connection
     */
    public Connection getConnection() {
        return connection;
    }
}
