import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.time.LocalDate;

public class LoanHandler implements HttpHandler {

    LoanService loanService = new LoanService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
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
        double amount = Double.parseDouble(extract(body, "amount"));
        String startDate = extract(body, "start_date");
        double rate = Double.parseDouble(extract(body, "rate"));
        int tenure = Integer.parseInt(extract(body, "tenure"));
        String accountNo = GenerateAccountNumber.generate();

        System.out.println("Parsed cust_id = " + custId);

        String loanId;
        try {
            loanId = loanService.createLoan(
                    custId,
                    accountNo,
                    amount,
                    LocalDate.parse(startDate),
                    rate,
                    tenure
            );
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        String response = "{ \"loan_id\": \"" + loanId + "\" }";

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

