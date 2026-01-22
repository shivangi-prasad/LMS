import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {

    private static final LoanRepository loanRepo = new LoanRepository();
    private static final InstallmentRepository installmentRepo = new InstallmentRepository();

    public static List<InstallmentScheduleDTO> getLoanSchedule(long customerId,
                                                               long loanId)
            throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            // 1️⃣ Validate loan belongs to customer
            boolean valid =
                    loanRepo.loanBelongsToCustomer(con, loanId, customerId);

            if (!valid) {
                throw new RuntimeException(
                        "Invalid loan/customer combination"
                );
            }

            // 2️⃣ Refresh DPD before returning schedule
            installmentRepo.updateDaysPastDue(con, loanId);

            // 3️⃣ Fetch unpaid / partial installments
            return installmentRepo
                    .findPendingInstallments(con, loanId);
        }
    }
}
