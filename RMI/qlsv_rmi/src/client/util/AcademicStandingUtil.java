package client.util;

public final class AcademicStandingUtil {
    private AcademicStandingUtil() {
    }

    public static String classifyByGpa(Double gpa) {
        if (gpa == null || Double.isNaN(gpa)) {
            return "-";
        }
        double value = gpa;
        if (value >= 9.0) {
            return "Xuất sắc";
        }
        if (value >= 8.0) {
            return "Giỏi";
        }
        if (value >= 7.0) {
            return "Khá";
        }
        if (value >= 6.0) {
            return "Trung bình khá";
        }
        if (value >= 5.0) {
            return "Trung bình";
        }
        if (value >= 4.0) {
            return "Yếu";
        }
        return "Kém";
    }
}
