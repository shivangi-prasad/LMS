import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class ScheduleHandler implements HttpHandler {

    ScheduleService loanService = new ScheduleService();

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

        String custIdStr = extract(body, "cust_id");
        String loanIdStr = extract(body, "loan_id");

        if (custIdStr == null || loanIdStr == null) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        long custId = Long.parseLong(custIdStr);
        long loanId = Long.parseLong(loanIdStr);

        System.out.println("Parsed cust_id = " + custId);
        System.out.println("Parsed loan_id = " + loanId);

        List<InstallmentScheduleDTO> schedule;
        try {
            schedule = ScheduleService.getLoanSchedule(custId, loanId);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        // 1️⃣ Print schedule like your example
        System.out.println("Pending Installments:");
        for (InstallmentScheduleDTO dto : schedule) {
            System.out.println(
                    dto.getInstallmentDueDate() + " | " +
                            dto.getDaysPastDue() + " | " +
                            dto.getStatus() + " | " +
                            dto.getInstallmentAmount()
            );
        }

        // 2️⃣ Build JSON response
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("[");

        for (InstallmentScheduleDTO dto : schedule) {
            responseBuilder.append("{")
                    .append("\"installment_due_date\": \"")
                    .append(dto.getInstallmentDueDate()).append("\",")
                    .append("\"dpd\": ")
                    .append(dto.getDaysPastDue()).append(",")
                    .append("\"status\": \"")
                    .append(dto.getStatus()).append("\",")
                    .append("\"installment_amount\": ")
                    .append(dto.getInstallmentAmount())
                    .append("},");
        }

        if (responseBuilder.charAt(responseBuilder.length() - 1) == ',') {
            responseBuilder.deleteCharAt(responseBuilder.length() - 1);
        }

        responseBuilder.append("]");

        String response = responseBuilder.toString();

        // 3️⃣ Send HTTP response
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
