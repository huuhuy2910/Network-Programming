package vn.edu.app.client.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static String format(Date d) { return d == null ? "" : SDF.format(d); }
    public static Date parse(String s) {
        try { return (s == null || s.isBlank()) ? null : SDF.parse(s); }
        catch (Exception e) { return null; }
    }
}
