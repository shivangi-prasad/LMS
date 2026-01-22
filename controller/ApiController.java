import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ApiController {

    private final LoanService loanService = new LoanService();

    // ==========================
    // Loan Creation API
    // ==========================
    public String loanCreation(long custId,
                               String accountNo,
                               double amt,
                               LocalDate start,
                               double rate,
                               int tenure) throws Exception {

        return loanService.createLoan(
                custId, accountNo, amt, start, rate, tenure
        );
    }

    // ==========================
    // Installment Payment API
    // ==========================
    public Long installmentPayment(Long loanId,
                                   String mode,
                                   double amt,
                                   LocalDateTime time) throws Exception {

        // delegate to service (DB generates payment_ref_id)
        return InstallmentPaymentService.installmentPayment(
                loanId, mode, amt, time
        );
    }

    // ==========================
    // Get Loan Details API
    // ==========================
    public Loan getLoanDetails(Long custId,
                               Long loanId) throws Exception {

        return loanService.getLoanDetails(
                custId, loanId
        );
    }

    // ==========================
    //  Get Loan Schedule API
    // ==========================
    public List<InstallmentScheduleDTO> getLoanSchedule(Long custId,
                                                        Long loanId)
            throws Exception {

        return ScheduleService.getLoanSchedule(custId, loanId);
    }
}
