package com.example.ecommerce.utils;
import java.security.SecureRandom;

public class PasswordUtils {
    private static final String CAPITAL_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "!@[]";
    private static final String NUMBERS = "1234567890";
    private static final int DEFAULT_LENGTH = 12;

    public static String generatePassword() {
        return generatePassword(DEFAULT_LENGTH);
    }

    public static String generatePassword(int length) {
        String combinedChars = CAPITAL_CASE_LETTERS + LOWER_CASE_LETTERS + SPECIAL_CHARACTERS + NUMBERS;
        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder(length);

        passwordBuilder.append(LOWER_CASE_LETTERS.charAt(random.nextInt(LOWER_CASE_LETTERS.length())));
        passwordBuilder.append(CAPITAL_CASE_LETTERS.charAt(random.nextInt(CAPITAL_CASE_LETTERS.length())));
        passwordBuilder.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
        passwordBuilder.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        for (int i = 4; i < length; i++) {
            passwordBuilder.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return passwordBuilder.toString();
    }
}
