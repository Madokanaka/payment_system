package kg.attractor.payment_system.util;

import java.util.Random;

public class AccountUtil {
    private static final int ACCOUNT_NUMBER_LENGTH = 20;

    public static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
}
