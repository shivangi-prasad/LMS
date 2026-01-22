import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class InstallmentRepository {

    public static void createInstallments(long loanId,
                                          LocalDate startDate,
                                          int tenureMonths,
                                          double emiAmount) throws Exception {

        String sql =
                "INSERT INTO lms.installment_details " +
                        "(installment_due_date, emi_amount, installment_amount, " +
                        " installment_status, days_past_due, payment_id, loan_id) " +
                        "VALUES (?, ?, ?, 'unpaid', 0, NULL, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // First installment = 1 month after start date
            LocalDate firstDueDate = startDate.plusMonths(1);

            int fixedDay = firstDueDate.getDayOfMonth();

            LocalDate dueDate = firstDueDate;

            for (int i = 0; i < tenureMonths; i++) {

                // Preserve day-of-month across all installments
                int lastDayOfMonth = dueDate.lengthOfMonth();

                int actualDay = Math.min(fixedDay, lastDayOfMonth);

                dueDate = dueDate.withDayOfMonth(actualDay);

                ps.setDate(1, Date.valueOf(dueDate));
                ps.setDouble(2, emiAmount);
                ps.setDouble(3, emiAmount);
                ps.setLong(4, loanId);

                ps.addBatch();

                // Next installment = +1 month from previous
                dueDate = dueDate.plusMonths(1);
            }

            ps.executeBatch();
        }
    }


    public Installment findOldestUnpaidInstallment(Connection con, long loanId)
            throws Exception {

        String sql =
                "SELECT * FROM lms.installment_details " +
                        "WHERE loan_id = ? AND installment_status IN ('unpaid', 'partial') " +
                        "ORDER BY installment_due_date ASC " +
                        "LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Installment i = new Installment();
            i.setInstallmentId(rs.getLong("installment_id"));
            i.setInstallmentAmount(rs.getDouble("installment_amount"));
            i.setStatus(rs.getString("installment_status"));
            i.setLoanId(rs.getLong("loan_id"));
            return i;
        }
    }

    public void markInstallmentPaid(Connection con,
                                    long installmentId,
                                    long paymentId) throws Exception {

        String sql =
                "UPDATE lms.installment_details i " +
                        "SET installment_status = 'paid', " +
                        "    installment_amount = 0, " +
                        "    payment_id = ?, " +
                        "    updated_date = p.payment_timestamp " +
                        "FROM lms.payment_table p " +
                        "WHERE i.installment_id = ? " +
                        "  AND p.payment_ref_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, paymentId);
            ps.setLong(2, installmentId);
            ps.setLong(3, paymentId);
            ps.executeUpdate();
        }
    }


    public void markInstallmentPartial(Connection con,
                                       long installmentId,
                                       double remainingAmount,
                                       long paymentId) throws Exception {

        String sql =
                "UPDATE lms.installment_details i " +
                        "SET installment_status = 'partial', " +
                        "    installment_amount = ?, " +
                        "    payment_id = ?, " +
                        "    updated_date = p.payment_timestamp " +
                        "FROM lms.payment_table p " +
                        "WHERE i.installment_id = ? " +
                        "  AND p.payment_ref_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, remainingAmount);
            ps.setLong(2, paymentId);
            ps.setLong(3, installmentId);
            ps.setLong(4, paymentId);
            ps.executeUpdate();
        }
    }


    public void updateDaysPastDue(Connection con, long loanId)
            throws Exception {

        String sql =
                "SELECT MAX(CURRENT_DATE - installment_due_date) AS max_dpd " +
                        "FROM lms.installment_details " +
                        "WHERE loan_id = ? AND installment_status IN ('unpaid', 'partial')";

        int maxDpd = 0;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                maxDpd = Math.max(0, rs.getInt("max_dpd"));
            }
        }

        String updateSql =
                "UPDATE lms.installment_details " +
                        "SET days_past_due = ? " +
                        "WHERE loan_id = ? AND installment_status IN ('unpaid', 'partial')";

        try (PreparedStatement ps = con.prepareStatement(updateSql)) {
            ps.setInt(1, maxDpd);
            ps.setLong(2, loanId);
            ps.executeUpdate();
        }
    }

    public List<InstallmentScheduleDTO> findPendingInstallments(
            Connection con, long loanId) throws Exception {

        String sql =
                "SELECT installment_due_date, " +
                        "       days_past_due, " +
                        "       installment_status, " +
                        "       installment_amount " +
                        "FROM lms.installment_details " +
                        "WHERE loan_id = ? " +
                        "  AND installment_status IN ('unpaid','partial') " +
                        "ORDER BY installment_due_date ASC";

        List<InstallmentScheduleDTO> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        new InstallmentScheduleDTO(
                                rs.getDate("installment_due_date").toLocalDate(),
                                rs.getInt("days_past_due"),
                                rs.getString("installment_status"),
                                rs.getDouble("installment_amount")
                        )
                );
            }
        }

        return list;
    }


}
