import java.time.LocalDate;
import java.util.UUID;

public class LoanService {

    private final LoanRepository loanRepository = new LoanRepository();
    private final CustomerRepository customerRepository = new CustomerRepository(); // you'll need this
    private final InstallmentRepository installmentRepo = new InstallmentRepository();
    private final PaymentRepository paymentRepo = new PaymentRepository();

    public String createLoan(long customerId,
                             String accountNo,
                             double amount,
                             LocalDate start,
                             double rate,
                             int tenure) throws Exception {

        boolean exists = customerRepository.customerExists(customerId);
        if (!exists) {
            throw new RuntimeException("Customer not found: " + customerId);
        }

            double emi = EmiCalculator.calculate(amount, rate, tenure);

            Loan loan = new Loan(
                    null,
                    accountNo,
                    customerId,
                    amount,
                    rate,
                    tenure,
                    start,
                    emi,
                    amount
            );

        long dbLoanId = loanRepository.saveLoan(loan);

        // 4. Generate installments
        InstallmentRepository.createInstallments(
                dbLoanId,
                start,
                tenure,
                emi
        );

            return String.valueOf(dbLoanId);
    }



    private final GetLoanDetailsService loanDetailsService =
            new GetLoanDetailsService();

    public Loan getLoanDetails(long customerId,
                               long loanId)
            throws Exception {

        return loanDetailsService.getLoanDetails(
                customerId, loanId
        );
    }
}
