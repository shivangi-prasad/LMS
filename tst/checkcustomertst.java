//package CustomerRepository;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class checkcustomertst {
//
//    public static void main(String[] args) throws Exception {
//
//        CustomerRepository customerRepository = new CustomerRepository();
//        Long customerId = 1L;
//        boolean exists = customerRepository.customerExists(customerId);
//
//        if(exists){
//            System.out.println("Customer " + "exists with CID : " + customerId);
//        }
//        else{
//            System.out.println("Customer " + "not exists");
//        }
//
//    }
//
//
//
//        public static void main(String[] args) throws Exception {
//
//            ApiController api = new ApiController();
//
//            System.out.println("Creation started");
//
//            long custid = 10L;
//            String loanId = api.loanCreation(
//                    custid,
//                    "ACC100007",
//                    90000,
//                    LocalDate.of(2026, 12, 15),
//                    10.5,
//                    12
//            );
//
//            System.out.println("Created loan: " + loanId);
//        }
//
//
//    public static void main(String[] args) {
//
//        try {
//            // 1️⃣ Test input values
//            Long loanId = 7L;                      // must exist in DB
//            String paymentMode = "upi";
//            double paymentAmount = 8000.00;       // arbitrary amount
//            LocalDateTime paymentTime = LocalDateTime.now();
//
//            // 2️⃣ Call API
//            LoanService service = new LoanService();
//            Long paymentRef  = InstallmentPaymentService.installmentPayment(
//                    loanId, paymentMode, paymentAmount, paymentTime
//            );
//
//            // 3️⃣ Print result
//            System.out.println("✅ Payment Successful");
//            System.out.println("Payment Reference: " + paymentRef);
//
//        } catch (Exception e) {
//            System.err.println("❌ Payment Failed");
//            e.printStackTrace();
//        }
//    }
//
//
//
//        public static void main(String[] args) throws Exception {
//
//            LoanService service = new LoanService();
//
//            long customerId = 10L;
//            long loanId = 7L;
//
//            List<InstallmentScheduleDTO> schedule =
//                    ScheduleService.getLoanSchedule(customerId, loanId);
//
//            System.out.println("Pending Installments:");
//
//            for (InstallmentScheduleDTO dto : schedule) {
//                System.out.println(
//                        dto.getInstallmentDueDate() + " | " +
//                                dto.getDaysPastDue() + " | " +
//                                dto.getStatus() + " | " +
//                                dto.getInstallmentAmount()
//                );
//            }
//        }
//
//
//        public static void main(String[] args) throws Exception {
//
//            LoanService service = new LoanService();
//
//            long customerId = 10L;
//            long loanId = 7L;
//
//            Loan loan = service.getLoanDetails(customerId, loanId);
//
//            System.out.println("Loan Details:");
//            System.out.println("Loan ID: " + loan.loanId);
//            System.out.println("Account No: " + loan.accountNo);
//            System.out.println("Customer ID: " + loan.customerId);
//            System.out.println("Sanctioned Amount: " + loan.sanctioned_amount);
//            System.out.println("Interest Rate: " + loan.annualRate);
//            System.out.println("Tenure: " + loan.tenureMonths);
//            System.out.println("Start Date: " + loan.startDate);
//            System.out.println("EMI: " + loan.emi);
//            System.out.println("Outstanding: " + loan.outstandingBalance);
//        }
//}
//
