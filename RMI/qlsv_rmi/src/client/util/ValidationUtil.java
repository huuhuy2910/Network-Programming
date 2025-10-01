package client.util;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+?84)\\d{8,10}$");

    private ValidationUtil() {
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email == null || EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone == null || PHONE_PATTERN.matcher(phone).matches();
    }
}
