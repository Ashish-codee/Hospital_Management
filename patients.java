package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to add a new patient
    public void addPatient() {
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine().trim();  // Read the patient's name and trim whitespace

        // Clear any leftover newline characters if they exist
        if (name.isEmpty()) {
            name = scanner.nextLine().trim();
        }

        int age = 0;
        while (true) {
            System.out.print("Enter Patient Age: ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character after the integer input
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid integer for age.");
                scanner.nextLine();  // Clear the invalid input
            }
        }

        System.out.print("Enter Patient Gender: ");
        String gender = scanner.nextLine().trim();  // Read the patient's gender

        // SQL query to insert the patient into the database
        String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient Added Successfully!!");
            } else {
                System.out.println("Failed to Add Patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to view all patients
    public void viewPatients() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patient List:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get patient details by ID for appointment booking
    public boolean getPatientById(int patientId) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();  // Returns true if patient exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
