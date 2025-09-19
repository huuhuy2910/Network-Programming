package vn.edu.app.server.util;

public class ValidationUtil {
    public static boolean isEmail(String email) {
        if (email == null || email.isBlank()) return true; // optional
        return email.matches("^[\\w.+\\-]+@([\\w\\-]+\\.)+[A-Za-z]{2,}$");
    }

    public static boolean isPhone(String phone) {
        if (phone == null || phone.isBlank()) return true; // optional
        return phone.matches("^\\d{9,12}$");
    }
}
