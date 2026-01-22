import java.time.LocalDate;

public class InstallmentScheduleDTO {

    private LocalDate installmentDueDate;
    private int daysPastDue;
    private String status;
    private double installmentAmount;

    public InstallmentScheduleDTO(LocalDate dueDate,
                                  int dpd,
                                  String status,
                                  double amount) {

        this.installmentDueDate = dueDate;
        this.daysPastDue = dpd;
        this.status = status;
        this.installmentAmount = amount;
    }

    public LocalDate getInstallmentDueDate() {
        return installmentDueDate;
    }

    public int getDaysPastDue() {
        return daysPastDue;
    }

    public String getStatus() {
        return status;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }
}
