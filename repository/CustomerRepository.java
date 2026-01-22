import java.sql.*;

public class CustomerRepository {

    public boolean customerExists(long customerId) throws Exception {

        String sql = "SELECT 1 FROM lms.customer_details WHERE customer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
