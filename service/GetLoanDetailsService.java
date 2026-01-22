import java.sql.Connection;

public class GetLoanDetailsService {

    private static final LoanRepository loanRepo = new LoanRepository();

    public static Loan getLoanDetails(long customerId,
                                      long loanId)
            throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            // 1. Validate loan belongs to customer
            boolean valid =
                    loanRepo.loanBelongsToCustomer(
                            con, loanId, customerId
                    );

            if (!valid) {
                throw new RuntimeException(
                        "Invalid loan/customer combination"
                );
            }

            // 2. Fetch full loan details
            Loan loan = loanRepo.findByLoanId(con, loanId);

            if (loan == null) {
                throw new RuntimeException(
                        "Loan not found: " + loanId
                );
            }

            return loan;
        }
    }
}
