import java.time.LocalDate;

public class Installment {
    private long installmentId;
    private LocalDate dueDate;
    private double emiAmount;
    private double installmentAmount;
    private String status;
    private int daysPastDue;
    private Long paymentId;
    private long loanId;
    private LocalDate updated_date;


    public long getInstallmentId() {
        return installmentId;
    }

    public void setInstallmentId(long installmentId) {
        this.installmentId = installmentId;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }
}