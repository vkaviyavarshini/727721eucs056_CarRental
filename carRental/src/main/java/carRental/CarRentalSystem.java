package carRental;

import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarRentalSystem {
    private ArrayList<Object> customers;
    private List<Car> cars;
    private List<BookingDetails> bookings;
    private Customer loggedInCustomer;
    private Scanner scanner;

    public CarRentalSystem() {
        customers = new ArrayList<>();
        cars = new ArrayList<>();
        bookings = new ArrayList<>();
        scanner = new Scanner(System.in);
        loggedInCustomer = null;
    }
    
    public Connection getConnection() throws SQLException {
        
        return DatabaseConnection.getConnection();
    }

    public void registerCustomer(String name, String address, String email, String password, int age, String gender, String phone, String licenseNo) {
        String sql = "INSERT INTO Customer (name, address, email, password, age, gender, phone, license_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, email);
            pstmt.setString(4, password); 
            pstmt.setInt(5, age);
            pstmt.setString(6, gender);
            pstmt.setString(7, phone);
            pstmt.setString(8, licenseNo);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Customer registration failed. Please try again.");
                return;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Customer registered successfully!");
                    System.out.println("Generated Customer ID: " + generatedId);
                    System.out.println("=============================");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while registering the customer: " + e.getMessage());
        }
    }

    public Customer login(String email, String password) {
        String sql = "SELECT * FROM Customer WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int custId = rs.getInt("cust_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String phone = rs.getString("phone");
                    String licenseNo = rs.getString("license_no");

                    Customer loggedInCustomer = new Customer(custId,name, address, email, password, age, gender, phone, licenseNo);
                    setLoggedInCustomer(loggedInCustomer);
                    return loggedInCustomer;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return null; 
    }
    
    private void setLoggedInCustomer(Customer customer) {
        loggedInCustomer = customer;
    }
    
    private boolean isLoggedIn() {
        return loggedInCustomer != null;
    }
    
    private Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }
    
    public void rentCar(int carId, Date returnDate) {
        if (!isLoggedIn()) {
            System.out.println("You need to log in first to rent a car.");
            return;
        }

        String sqlBooking = "INSERT INTO BookingDetails (return_date, cust_id, car_id) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmtBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS)) {
          
            pstmtBooking.setDate(1, returnDate);
            pstmtBooking.setInt(2, loggedInCustomer.getCustId());
            pstmtBooking.setInt(3, carId);

            int affectedRows = pstmtBooking.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Booking car failed. Please try again.");
                return;
            }

            try (ResultSet generatedKeys = pstmtBooking.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedBookingId = generatedKeys.getInt(1);
                    System.out.println("Car rented successfully!");
                    System.out.println("Booking ID: " + generatedBookingId);

                    String sqlUpdateCar = "UPDATE Car SET availability = false WHERE car_id = ?";
                    try (PreparedStatement pstmtUpdateCar = conn.prepareStatement(sqlUpdateCar)) {
                        pstmtUpdateCar.setInt(1, carId);
                        pstmtUpdateCar.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while renting the car: " + e.getMessage());
        }
       
        BookingDetails booking = new BookingDetails( returnDate, loggedInCustomer.getCustId(), carId);
        // Save the booking details to the database or perform any other necessary actions
        System.out.println("Return Date: " + booking.getReturnDate());
        System.out.println("Customer ID: " + booking.getCustId());
        System.out.println("Car ID: " + booking.getCarId());
    }

    public void returnCar(int bookId) {
        if (loggedInCustomer == null) {
            System.out.println("Please log in first to return a car.");
            return;
        }

        String sqlGetBooking = "SELECT * FROM BookingDetails WHERE book_id = ? AND cust_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmtGetBooking = conn.prepareStatement(sqlGetBooking)) {
            pstmtGetBooking.setInt(1, bookId);
            pstmtGetBooking.setInt(2, loggedInCustomer.getCustId());

            try (ResultSet rs = pstmtGetBooking.executeQuery()) {
                if (rs.next()) {
                    int carId = rs.getInt("car_id");

                    // Update car availability to true
                    String sqlUpdateCar = "UPDATE Car SET availability = true WHERE car_id = ?";
                    try (PreparedStatement pstmtUpdateCar = conn.prepareStatement(sqlUpdateCar)) {
                        pstmtUpdateCar.setInt(1, carId);
                        pstmtUpdateCar.executeUpdate();
                    }

                    System.out.println("\nCar returned successfully!\n");

                    // Calculate and display billing details
                    double rentalRatePerDay = 1000.0;
                    Date rentalDate = rs.getDate("return_date");
                    Date currentDate = new Date(System.currentTimeMillis());
                    long rentalDays = (rentalDate.getTime() - currentDate.getTime()) / (24 * 60 * 60 * 1000);
                    double totalAmount = 1000+(rentalDays * rentalRatePerDay);

                    System.out.println("Billing Details:\n---------------");
                    System.out.println("Booking ID: " + bookId);
                    System.out.println("Customer ID: " + loggedInCustomer.getCustId());
                    System.out.println("Customer Name: " + loggedInCustomer.getName());
                    System.out.println("Total Amount: ₹" + totalAmount);
                } else {
                    System.out.println("Invalid booking ID or the booking does not belong to you.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while returning the car: " + e.getMessage());
        }
    }

    public List<Car> viewAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE availability = true";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String model = rs.getString("model");
                String brand = rs.getString("brand");
                boolean availability = rs.getBoolean("availability");

                Car car = new Car(carId, model, brand, availability);
                availableCars.add(car);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching available cars: " + e.getMessage());
        }
        return availableCars;
    }

    public void logout() {
    	System.out.println("Logout Successful");
        loggedInCustomer = null;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public static void main(String[] args) {
        CarRentalSystem carRentalSystem = new CarRentalSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Car Rental System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Available Cars");
            System.out.println("4. Rent a Car");
            System.out.println("5. Return a Car");
            System.out.println("6. Logout");
            System.out.println("7. Exit");

            int choice;
            try {
                System.out.print("\nEnter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Enter customer name:");
                    String name = scanner.nextLine();

                    System.out.println("Enter customer address:");
                    String address = scanner.nextLine();

                    System.out.println("Enter customer email:");
                    String email = scanner.nextLine();
                    
                    System.out.println("Enter customer password:");
                    String password = scanner.nextLine();

                    System.out.println("Enter customer age:");
                    int age = scanner.nextInt();
                    scanner.nextLine(); 

                    System.out.println("Enter customer gender:");
                    String gender = scanner.nextLine();

                    System.out.println("Enter customer phone number:");
                    String phone = scanner.nextLine();

                    System.out.println("Enter customer license number:");
                    String licenseNo = scanner.nextLine();

                    carRentalSystem.registerCustomer(name, address, email, password, age, gender, phone, licenseNo); 
                    break;
             
                case 2:
                    System.out.println("Enter your email:");
                    String email1 = scanner.next();

                    System.out.println("Enter your password:");
                    String password1 = scanner.next();

                    Customer loggedInCustomer = carRentalSystem.login(email1, password1); //encapsulation
                    if (loggedInCustomer != null) {
                        System.out.println("Login successful!!!.\n\n Welcome, " + loggedInCustomer.getName() + "!");
                        System.out.println("===================");
                    } else {
                        System.out.println("Login failed. Invalid email or password.");
                    }
                    break;
                    
                case 3:
                    List<Car> availableCars = carRentalSystem.viewAvailableCars(); //encapsulation
                    System.out.println("\nAvailable Cars:");
                    for (Car car : availableCars) {
                        System.out.println("Car ID: " + car.getVehicleId() + "	Brand: " + car.getBrand() + "		Model: " + car.getModel());
                    }
                    System.out.println("======================================================");
                    break;
                    
                case 4:
                    System.out.println("Enter Car ID:");
                    int carId = scanner.nextInt();
                    System.out.println("Enter Return Date (yyyy-mm-dd):");
                    String returnDateStr = scanner.next();
                    Date returnDate = Date.valueOf(returnDateStr);
                    carRentalSystem.rentCar(carId, returnDate); //encapsulation
                    System.out.println("===========================");
                    break;
                    
                case 5:
                	System.out.println("Enter Booking ID:");
                    int bookId = scanner.nextInt();
                    carRentalSystem.returnCar(bookId); //encapsulation
                    System.out.println("===========================");
                    break;
                    
                case 6:
                    carRentalSystem.logout();
                    System.out.println("===========================");
                    break;
                    
                case 7:
                    System.out.println("Thank you for using the Car Rental System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
