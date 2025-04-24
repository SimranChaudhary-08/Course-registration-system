import java.sql.*;
import java.util.Scanner;

public class CourseRegistrationWithDB {
    static final String URL = "jdbc:mysql://localhost:3306/course_registration";
    static final String USER = "root"; // change to your MySQL username
    static final String PASS = "password"; // change to your MySQL password

    static Connection conn;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            conn = DriverManager.getConnection(URL, USER, PASS);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your student ID: ");
            String studentId = scanner.nextLine();

            int studentDbId = insertStudent(name, studentId);

            int choice;
            do {
                System.out.println("\nMenu:");
                System.out.println("1. View Courses");
                System.out.println("2. Register for a Course");
                System.out.println("3. View Registered Courses");
                System.out.println("4. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        viewCourses();
                        break;
                    case 2:
                        System.out.print("Enter course ID to register: ");
                        int courseId = scanner.nextInt();
                        registerCourse(studentDbId, courseId);
                        break;
                    case 3:
                        viewRegisteredCourses(studentDbId);
                        break;
                    case 4:
                        System.out.println("Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } while (choice != 4);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    static int insertStudent(String name, String studentId) throws SQLException {
        String sql = "INSERT INTO student (name, student_id) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setString(2, studentId);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return -1;
    }

    static void viewCourses() throws SQLException {
        String sql = "SELECT * FROM course";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Available Courses:");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + ". " +
                               rs.getString("course_code") + " - " +
                               rs.getString("course_name"));
        }
    }

    static void registerCourse(int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO registration (student_id, course_id) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Course registered successfully.");
        }
    }

    static void viewRegisteredCourses(int studentId) throws SQLException {
        String sql = "SELECT c.course_code, c.course_name FROM registration r " +
                     "JOIN course c ON r.course_id = c.id " +
                     "WHERE r.student_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, studentId);
        ResultSet rs = ps.executeQuery();
        System.out.println("Registered Courses:");
        while (rs.next()) {
            System.out.println(rs.getString("course_code") + " - " + rs.getString("course_name"));
        }
    }
}
