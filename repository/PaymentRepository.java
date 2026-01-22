import java.sql.*;

public class PaymentRepository {

    public long savePayment(Connection con, Payment payment)
            throws Exception {

        String sql =
                "INSERT INTO lms.payment_table " +
                        "(payment_gateway, payment_mode, payment_amount, payment_status) " +
                        "VALUES (?, ?, ?, ?) RETURNING payment_ref_id";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, payment.getPaymentGateway());
            ps.setString(2, payment.getPaymentMode());
            ps.setDouble(3, payment.getPaymentAmount());
            ps.setString(4, payment.getPaymentStatus());

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);   // ✅ DB-generated BIGINT
        }
    }

}
