import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.time.LocalDate;

public class GetLoanHandler implements HttpHandler {

    GetLoanDetailsService loanService = new GetLoanDetailsService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody())
        );

        StringBuilder bodyBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            bodyBuilder.append(line);
        }
        String body = bodyBuilder.toString();
        br.close();

        System.out.println("RAW BODY = " + body);

        long custId = Long.parseLong(extract(body, "cust_id"));
        long loanId = Long.parseLong(extract(body, "loan_id"));


        Loan loan;
        try {
            loan = GetLoanDetailsService.getLoanDetails(custId,loanId );
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        System.out.println("LOAN = " + loan.loanId);

        String response = "{"
                + "\"loan_id\": " + loan.loanId + ","
                + "\"account_no\": \"" + loan.accountNo + "\","
                + "\"customer_id\": " + loan.customerId + ","
                + "\"sanctioned_amount\": " + loan.sanctioned_amount + ","
                + "\"interest_rate\": " + loan.annualRate + ","
                + "\"tenure\": " + loan.tenureMonths + ","
                + "\"start_date\": \"" + loan.startDate + "\","
                + "\"emi\": " + loan.emi + ","
                + "\"outstanding_balance\": " + loan.outstandingBalance
                + "}";


        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String extract(String body, String key) {
        int keyIndex = body.indexOf("\"" + key + "\"");
        if (keyIndex == -1) return null;

        int colonIndex = body.indexOf(":", keyIndex);
        int commaIndex = body.indexOf(",", colonIndex);

        if (commaIndex == -1) {
            commaIndex = body.indexOf("}", colonIndex);
        }

        String rawValue = body.substring(colonIndex + 1, commaIndex).trim();
        rawValue = rawValue.replaceAll("[\"}]", "").trim();

        return rawValue;
    }
}
