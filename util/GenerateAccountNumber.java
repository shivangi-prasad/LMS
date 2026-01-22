import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateAccountNumber {

    private static final String PREFIX = "ACC";
    private static final SecureRandom RANDOM = new SecureRandom();

    // Counter to avoid collision when called rapidly in the same millisecond
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * Generates a unique account number each time.
     * Format: ACC + 7 digits
     */
    public static String generate() {

        long timestampPart = Instant.now().toEpochMilli() % 1_000_000; // last 6 digits
        int randomPart = RANDOM.nextInt(10); // 0–9
        int counterPart = COUNTER.getAndIncrement() % 10; // 0–9

        String number = String.format("%06d%d%d",
                timestampPart,
                randomPart,
                counterPart
        );

        return PREFIX + number;
    }
}