package vn.edu.app.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Sửa các hằng số phù hợp môi trường của bạn
    private static final String URL = "jdbc:mysql://localhost:3306/student_mgmt?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASS = "1234"; // đổi mật khẩu thật

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // nạp driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
