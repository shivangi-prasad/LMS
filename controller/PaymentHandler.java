import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.time.LocalDateTime;

public class PaymentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Check 1 ");
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

        Long loanId = Long.parseLong(extract(body, "loan_id"));
        String mode = extract(body, "payment_mode");
        Double amount = Double.parseDouble(extract(body, "payment_amount"));
        String ts = extract(body, "timestamp");


        System.out.println("Parsed loan_id = " + loanId);
        System.out.println("Parsed amount = " + amount);
        System.out.println("Parsed timestamp = " + ts);

        Long paymentId;


            paymentId = InstallmentPaymentService.installmentPayment(
                    loanId,
                    mode,
                    amount,
                    LocalDateTime.parse(ts)
            );

            String response = "{ \"payment_id\": \"" + paymentId + "\" }";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.getResponseBody().close();
            return;
        }
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
