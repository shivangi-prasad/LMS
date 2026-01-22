import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/lms_db";

    private static final String USER = "lms_user";
    private static final String PASSWORD = "lms_pass123";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Postgres JDBC driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(" Postgres JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
