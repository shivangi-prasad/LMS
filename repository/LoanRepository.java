import java.sql.*;

public class LoanRepository {

    public long saveLoan(Loan loan) throws Exception {

        String sql =
                "INSERT INTO lms.loan_details " +
                        "(customer_id, account_no, sanctioned_amount, " +
                        " loan_period, loan_start_date, annual_interest_rate, " +
                        " emi_per_month, outstanding_balance) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING loan_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, loan.customerId);
            ps.setString(2, loan.accountNo);
            ps.setDouble(3, loan.sanctioned_amount);
            ps.setInt(4, loan.tenureMonths);
            ps.setDate(5, Date.valueOf(loan.startDate));
            ps.setDouble(6, loan.annualRate);
            ps.setDouble(7, loan.emi);
            ps.setDouble(8, loan.outstandingBalance);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);   // DB-generated loan_id
        }
    }

    public Loan findByLoanId(Connection con, long loanId)
            throws Exception {

        String sql =
                "SELECT * FROM lms.loan_details WHERE loan_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Loan(
                    rs.getLong("loan_id"),
                    rs.getString("account_no"),
                    rs.getLong("customer_id"),
                    rs.getDouble("sanctioned_amount"),
                    rs.getDouble("annual_interest_rate"),
                    rs.getInt("loan_period"),
                    rs.getDate("loan_start_date").toLocalDate(),
                    rs.getDouble("emi_per_month"),
                    rs.getDouble("outstanding_balance")
            );
        }
    }


    public void reduceOutstandingBalance(Connection con,
                                         long loanId,
                                         double paidAmount) throws Exception {

        String sql =
                "UPDATE lms.loan_details " +
                        "SET outstanding_balance = outstanding_balance - ? " +
                        "WHERE loan_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, paidAmount);
            ps.setLong(2, loanId);
            ps.executeUpdate();
        }
    }

    public double getOutstandingBalance(Connection con, long loanId)
            throws Exception {

        String sql =
                "SELECT outstanding_balance FROM lms.loan_details WHERE loan_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 0;
            return rs.getDouble(1);
        }
    }

    public boolean loanBelongsToCustomer(Connection con,
                                         long loanId,
                                         long customerId) throws Exception {

        String sql =
                "SELECT 1 FROM lms.loan_details " +
                        "WHERE loan_id = ? AND customer_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ps.setLong(2, customerId);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

}
