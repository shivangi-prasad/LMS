public class EmiCalculator {
    public static double calculate(double p, double rate, int months) {
        double r = rate / (12 * 100);
        return (p * r * Math.pow(1 + r, months)) /
                (Math.pow(1 + r, months) - 1);
    }
}