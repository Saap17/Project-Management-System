package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ProjectManagementSystem {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectdb", "root", "root");
        Scanner sc = new Scanner(System.in);
        int operation; //declares an integer to variable to store the users chosen operation 

        do {
            System.out.println("Available Operations:");
            System.out.println("1. Create Project");
            System.out.println("2. Retrieve Projects");
            System.out.println("3. Update Project");
            System.out.println("4. Delete Project");
            System.out.println("---------------------------------");
            System.out.println("5. Create Task");
            System.out.println("6. Retrieve Tasks");
            System.out.println("7. Update Task");
            System.out.println("8. Delete Task");
            System.out.println("---------------------------------");
            System.out.println(" 9. Create Employee");
            System.out.println("10. Retrieve Employees");
            System.out.println("11. Update Employee");
            System.out.println("12. Delete Employee");
            System.out.println("13. Exit \n");
            System.out.print("Please Enter an Operation number you want to Perform: ");
            operation = sc.nextInt();
            sc.nextLine();

            switch (operation) { //handles different operations based on user input
                case 1:
                    createProject(connection, sc);
                    break;
                case 2:
                    retrieveProjects(connection);
                    break;
                case 3:
                    updateProject(connection, sc);
                    break;
                case 4:
                    deleteProject(connection, sc);
                    break;
                case 5:
                    createTask(connection, sc);
                    break;
                case 6:
                    retrieveTasks(connection);
                    break;
                case 7:
                    updateTask(connection, sc);
                    break;
                case 8:
                    deleteTask(connection, sc);
                    break;
                case 9:
                    createEmployee(connection, sc);
                    break;
                case 10:
                    retrieveEmployees(connection);
                    break;
                case 11:
                    updateEmployee(connection, sc);
                    break;
                case 12:
                    deleteEmployee(connection, sc);
                    break;
                case 13:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (operation != 13);

        sc.close(); //close the scanner object connection.
        connection.close();
    }

    // Methods for Project CRUD Operations
    private static void createProject(Connection connection, Scanner sc) throws SQLException { 
        System.out.print("Enter Project ID: ");
        int projectID = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter project name: ");
        String projectName = sc.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = sc.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = sc.nextLine();
        System.out.print("Enter status: ");
        String status = sc.nextLine();

        String createSql = "INSERT INTO Project (ProjectID, ProjectName, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement createStatement = connection.prepareStatement(createSql)) {
            createStatement.setInt(1, projectID);
            createStatement.setString(2, projectName);
            createStatement.setString(3, startDate);
            createStatement.setString(4, endDate);
            createStatement.setString(5, status);

            int rowsAffected = createStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Project added successfully.");
            } else {
                System.out.println("Failed to add project.");
            }
        }
    }

    private static void retrieveProjects(Connection connection) throws SQLException {
        String readSql = "SELECT * FROM Project";
        try (Statement readStatement = connection.createStatement()) {
            ResultSet resultSet = readStatement.executeQuery(readSql);
            while (resultSet.next()) {
                int projectId = resultSet.getInt("ProjectID");
                String projectName = resultSet.getString("ProjectName");
                String startDate = resultSet.getString("StartDate");
                String endDate = resultSet.getString("EndDate");
                String status = resultSet.getString("Status");
                System.out.println(
                        "ID: " + projectId + ", Name: " + projectName + ", Start Date: " + startDate + ", End Date: " + endDate + ", Status: " + status);
            }
        }
    }

    private static void updateProject(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter project ID to update: ");
        int projectIdToUpdate = sc.nextInt();
        sc.nextLine();

        System.out.println("Choose what to update:");
        System.out.println("1. Update name");
        System.out.println("2. Update start date");
        System.out.println("3. Update end date");
        System.out.println("4. Update status");
        System.out.print("Enter your choice: ");
        int updateChoice = sc.nextInt();
        sc.nextLine();

        String updateSql;
        PreparedStatement updateStatement;
        switch (updateChoice) {
            case 1:
                System.out.print("Enter new project name: ");
                String newProjectName = sc.nextLine();
                updateSql = "UPDATE Project SET ProjectName = ? WHERE ProjectID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newProjectName);
                break;
            case 2:
                System.out.print("Enter new start date (YYYY-MM-DD): ");
                String newStartDate = sc.nextLine();
                updateSql = "UPDATE Project SET StartDate = ? WHERE ProjectID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newStartDate);
                break;
            case 3:
                System.out.print("Enter new end date (YYYY-MM-DD): ");
                String newEndDate = sc.nextLine();
                updateSql = "UPDATE Project SET EndDate = ? WHERE ProjectID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newEndDate);
                break;
            case 4:
                System.out.print("Enter new status: ");
                String newStatus = sc.nextLine();
                updateSql = "UPDATE Project SET Status = ? WHERE ProjectID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newStatus);
                break;
            default:
                System.out.println("Invalid choice for update. Please try again.");
                return;
        }

        updateStatement.setInt(2, projectIdToUpdate);
        int rowsAffected = updateStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Project updated successfully.\n");
        } else {
            System.out.println("Project not found or update failed.");
        }
    }

    private static void deleteProject(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter project ID to delete: ");
        int projectIdToDelete = sc.nextInt();

        String deleteSql = "DELETE FROM Project WHERE ProjectID = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, projectIdToDelete);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Project deleted successfully.\n");
            } else {
                System.out.println("Project not found or delete failed.");
            }
        }
    }

    // Methods for Task CRUD Operations
    private static void createTask(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter project ID: ");
        int projectId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter task name: ");
        String taskName = sc.nextLine();
        System.out.print("Enter assigned employee ID: ");
        int assignedTo = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = sc.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = sc.nextLine();

        String createSql = "INSERT INTO Task (ProjectID, TaskName, AssignedTo, StartDate, EndDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement createStatement = connection.prepareStatement(createSql)) {
            createStatement.setInt(1, projectId);
            createStatement.setString(2, taskName);
            createStatement.setInt(3, assignedTo);
            createStatement.setString(4, startDate);
            createStatement.setString(5, endDate);

            int rowsAffected = createStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task added successfully.\n ");
            } else {
                System.out.println("Failed to add task.");
            }
        }
    }

    private static void retrieveTasks(Connection connection) throws SQLException {
        String readSql = "SELECT * FROM Task";
        try (Statement readStatement = connection.createStatement()) {
            ResultSet resultSet = readStatement.executeQuery(readSql);
            while (resultSet.next()) {
                int taskId = resultSet.getInt("TaskID");
                int projectId = resultSet.getInt("ProjectID");
                String taskName = resultSet.getString("TaskName");
                int assignedTo = resultSet.getInt("AssignedTo");
                String startDate = resultSet.getString("StartDate");
                String endDate = resultSet.getString("EndDate");
                System.out.println("Task ID: " + taskId + ", Project ID: " + projectId + ", Name: " + taskName + ", Assigned To: " + assignedTo + ", Start Date: " + startDate + ", End Date: " + endDate);
            }
        }
    }

    private static void updateTask(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter task ID to update: ");
        int taskIdToUpdate = sc.nextInt();
        sc.nextLine();

        System.out.println("Choose what to update:");
        System.out.println("1. Update task name");
        System.out.println("2. Update assigned employee ID");
        System.out.println("3. Update start date");
        System.out.println("4. Update end date");
        System.out.print("Enter your choice: ");
        int updateChoice = sc.nextInt();
        sc.nextLine();

        String updateSql;
        PreparedStatement updateStatement;
        switch (updateChoice) {
            case 1:
                System.out.print("Enter new task name: ");
                String newTaskName = sc.nextLine();
                updateSql = "UPDATE Task SET TaskName = ? WHERE TaskID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newTaskName);
                break;
            case 2:
                System.out.print("Enter new assigned employee ID: ");
                int newAssignedTo = sc.nextInt();
                updateSql = "UPDATE Task SET AssignedTo = ? WHERE TaskID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setInt(1, newAssignedTo);
                break;
            case 3:
                System.out.print("Enter new start date (YYYY-MM-DD): ");
                String newStartDate = sc.nextLine();
                updateSql = "UPDATE Task SET StartDate = ? WHERE TaskID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newStartDate);
                break;
            case 4:
                System.out.print("Enter new end date (YYYY-MM-DD): ");
                String newEndDate = sc.nextLine();
                updateSql = "UPDATE Task SET EndDate = ? WHERE TaskID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newEndDate);
                break;
            default:
                System.out.println("Invalid choice for update. Please try again.");
                return;
        }

        updateStatement.setInt(2, taskIdToUpdate);
        int rowsAffected = updateStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Task updated successfully.\n ");
        } else {
            System.out.println("Task not found or update failed.");
        }
    }

    private static void deleteTask(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter task ID to delete: ");
        int taskIdToDelete = sc.nextInt();

        String deleteSql = "DELETE FROM Task WHERE TaskID = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, taskIdToDelete);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Task not found or delete failed.");
            }
        }
    }

    // Methods for Employee CRUD Operations
    private static void createEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter employee name: ");
        String employeeName = sc.nextLine();
        System.out.print("Enter contact info: ");
        String contactInfo = sc.nextLine();
        System.out.print("Enter role: ");
        String role = sc.nextLine();

        String createSql = "INSERT INTO Employee (Name, ContactInfo, Role) VALUES (?, ?, ?)";
        try (PreparedStatement createStatement = connection.prepareStatement(createSql)) {
            createStatement.setString(1, employeeName);
            createStatement.setString(2, contactInfo);
            createStatement.setString(3, role);

            int rowsAffected = createStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        }
    }

    private static void retrieveEmployees(Connection connection) throws SQLException {
        String readSql = "SELECT * FROM Employee";
        try (Statement readStatement = connection.createStatement()) {
            ResultSet resultSet = readStatement.executeQuery(readSql);
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("EmployeeID");
                String employeeName = resultSet.getString("Name");
                String contactInfo = resultSet.getString("ContactInfo");
                String role = resultSet.getString("Role");
                System.out.println("Employee ID: " + employeeId + ", Name: " + employeeName + ", Contact Info: " + contactInfo + ", Role: " + role);
            }
        }
    }

    private static void updateEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int employeeIdToUpdate = sc.nextInt();
        sc.nextLine();

        System.out.println("Choose what to update:");
        System.out.println("1. Update name");
        System.out.println("2. Update contact info");
        System.out.println("3. Update role");
        System.out.print("Enter your choice: ");
        int updateChoice = sc.nextInt();
        sc.nextLine();

        String updateSql;
        PreparedStatement updateStatement;
        switch (updateChoice) {
            case 1:
                System.out.print("Enter new employee name: ");
                String newEmployeeName = sc.nextLine();
                updateSql = "UPDATE Employee SET Name = ? WHERE EmployeeID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newEmployeeName);
                break;
            case 2:
                System.out.print("Enter new contact info: ");
                String newContactInfo = sc.nextLine();
                updateSql = "UPDATE Employee SET ContactInfo = ? WHERE EmployeeID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newContactInfo);
                break;
            case 3:
                System.out.print("Enter new role: ");
                String newRole = sc.nextLine();
                updateSql = "UPDATE Employee SET Role = ? WHERE EmployeeID = ?";
                updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, newRole);
                break;
            default:
                System.out.println("Invalid choice for update. Please try again.");
                return;
        }

        updateStatement.setInt(2, employeeIdToUpdate);
        int rowsAffected = updateStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Employee not found or update failed.");
        }
    }

    private static void deleteEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int employeeIdToDelete = sc.nextInt();

        String deleteSql = "DELETE FROM Employee WHERE EmployeeID = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, employeeIdToDelete);
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not found or delete failed.");
            }
        }
    }
}
