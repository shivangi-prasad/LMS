import java.time.LocalDate;

public class Loan {

    public String accountNo;
    public Long loanId;
    public Long customerId;
    public double sanctioned_amount;
    public double outstandingBalance;
    public double annualRate;
    public int tenureMonths;
    public LocalDate startDate;
    public double emi;

    public Loan(Long loanId,
                String accountNo,
                Long customerId,
                double sanctioned_amount,
                double annualRate,
                int tenureMonths,
                LocalDate startDate,
                double emi,
                double outstandingBalance) {

        this.accountNo = accountNo;
        this.loanId = loanId;
        this.customerId = customerId;
        this.sanctioned_amount = sanctioned_amount;
        this.annualRate = annualRate;
        this.tenureMonths = tenureMonths;
        this.startDate = startDate;
        this.emi = emi;
        this.outstandingBalance = outstandingBalance;
    }
}
