package carRental;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "kaviya@2000";

    public static Connection getConnection() throws SQLException {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the JDBC driver
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found. Please check the driver classpath.");
            e.printStackTrace();
            return null;
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
