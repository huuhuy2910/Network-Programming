package vn.edu.app.client.util;

public class Validators {
    public static boolean isEmail(String s) {
        return s == null || s.isBlank() || s.matches("^[\\w.+\\-]+@([\\w\\-]+\\.)+[A-Za-z]{2,}$");
    }
    public static boolean isPhone(String s) {
        return s == null || s.isBlank() || s.matches("^\\d{9,12}$");
    }
}
