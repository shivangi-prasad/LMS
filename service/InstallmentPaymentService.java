import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.UUID;

public class InstallmentPaymentService {

    private static final InstallmentRepository installmentRepo = new InstallmentRepository();
    private static final PaymentRepository paymentRepo = new PaymentRepository();
    private static final LoanRepository loanRepo = new LoanRepository();


    public static Long installmentPayment(
            long loanId,
            String paymentMode,
            double paymentAmount,
            LocalDateTime time
    ) throws Exception {

        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);

            // 1. Block overpayment
            double totalOutstanding =
                    loanRepo.getOutstandingBalance(con, loanId);

            if (paymentAmount > totalOutstanding) {
                throw new RuntimeException(
                        "Payment exceeds total outstanding balance: " +
                                totalOutstanding
                );
            }

            // 2. Create payment record
//            String paymentRef = UUID.randomUUID().toString();

            Payment payment = new Payment(
                    null,
                    "INTERNAL",
                    paymentMode,
                    paymentAmount,
                    "success",
                    LocalDateTime.now()
            );

            long paymentId = paymentRepo.savePayment(con, payment);

            // 3. Allocate across installments
            double remainingPayment = paymentAmount;

            while (remainingPayment > 0) {

                Installment inst =
                        installmentRepo.findOldestUnpaidInstallment(con, loanId);

                if (inst == null) break;

                double installmentDue = inst.getInstallmentAmount();
                double applied = Math.min(remainingPayment, installmentDue);
                double newRemaining = installmentDue - applied;

                if (Math.abs(newRemaining) < 0.01) {
                    installmentRepo.markInstallmentPaid(
                            con, inst.getInstallmentId(), paymentId
                    );
                } else {
                    installmentRepo.markInstallmentPartial(
                            con, inst.getInstallmentId(), newRemaining, paymentId
                    );
                }

                remainingPayment -= applied;

                loanRepo.reduceOutstandingBalance(
                        con, loanId, applied
                );
            }

            installmentRepo.updateDaysPastDue(con, loanId);

            con.commit();
            return paymentId;

        } catch (Exception ex) {
            con.rollback();
            throw ex;

        } finally {
            con.close();
        }
    }
}
