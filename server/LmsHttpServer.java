import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class LmsHttpServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/loans", new GetLoanHandler());

        server.createContext("/payments", new PaymentHandler());

        server.createContext("/getloandetails", new GetLoanHandler());

        server.createContext("/getloanSchedule", new ScheduleHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("🚀 LMS Server running on http://localhost:8080");
    }
}
