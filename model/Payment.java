import java.time.LocalDateTime;

public class Payment {

    private Long paymentRefId;
    private String paymentGateway;
    private String paymentMode;
    private double paymentAmount;
    private String paymentStatus;
    private LocalDateTime paymentTimestamp;

    public Payment(Long paymentRefId,
                   String paymentGateway,
                   String paymentMode,
                   double paymentAmount,
                   String paymentStatus,
                   LocalDateTime paymentTimestamp) {

        this.paymentRefId = paymentRefId;
        this.paymentGateway = paymentGateway;
        this.paymentMode = paymentMode;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.paymentTimestamp = paymentTimestamp;
    }


    public Long getPaymentRefId() {
        return paymentRefId;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentTimestamp() {
        return paymentTimestamp;
    }

    public void setPaymentRefId(Long paymentRefId) {
        this.paymentRefId = paymentRefId;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentTimestamp(LocalDateTime paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
    }
}
