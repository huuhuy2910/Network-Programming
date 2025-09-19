package vn.edu.app.client.remote;

import vn.edu.app.common.remote.*;

import java.rmi.Naming;

public class RMIConnector {
    private static final String HOST = "localhost"; // chỉnh nếu server khác máy
    private static final int PORT = 1099;

    private static String url(String name) {
        return String.format("rmi://%s:%d/%s", HOST, PORT, name);
    }

    public static AuthService auth() throws Exception {
        return (AuthService) Naming.lookup(url("AuthService"));
    }

    public static StudentService student() throws Exception {
        return (StudentService) Naming.lookup(url("StudentService"));
    }

    public static AdminService admin() throws Exception {
        return (AdminService) Naming.lookup(url("AdminService"));
    }

    public static ReportService report() throws Exception {
        return (ReportService) Naming.lookup(url("ReportService"));
    }
}
