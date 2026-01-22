import java.sql.Connection;

public class JdbcTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection c = DBConnection.getConnection();
        System.out.println("Connected to Postgres!");
    }
}
