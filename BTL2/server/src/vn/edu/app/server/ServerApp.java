package vn.edu.app.server;

import vn.edu.app.server.dao.*;
import vn.edu.app.server.dao.impl.*;
import vn.edu.app.server.service.*;
import vn.edu.app.common.remote.*;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerApp {
    public static void main(String[] args) {
        try {
            // 1) Khởi tạo DAO
            StudentDAO studentDAO = new StudentDAOImpl();
            ClassDAO classDAO = new ClassDAOImpl();
            MajorDAO majorDAO = new MajorDAOImpl();
            CourseDAO courseDAO = new CourseDAOImpl();
            GradeDAO gradeDAO = new GradeDAOImpl();
            UserDAO userDAO = new UserDAOImpl();

            // 2) Khởi tạo Service Impl
            AuthService authService = new AuthServiceImpl(userDAO);
            StudentService studentService = new StudentServiceImpl(studentDAO);
            AdminService adminService = new AdminServiceImpl(studentDAO, classDAO, majorDAO, courseDAO, gradeDAO);
            ReportService reportService = new ReportServiceImpl();

            // 3) Khởi động RMI Registry (nếu chưa chạy)
            try { LocateRegistry.createRegistry(1099); } catch (Exception ignore) {}

            // 4) Bind
            Naming.rebind("rmi://localhost:1099/AuthService", authService);
            Naming.rebind("rmi://localhost:1099/StudentService", studentService);
            Naming.rebind("rmi://localhost:1099/AdminService", adminService);
            Naming.rebind("rmi://localhost:1099/ReportService", reportService);

            System.out.println("✅ RMI Server started. Services bound:");
            System.out.println(" - rmi://localhost:1099/AuthService");
            System.out.println(" - rmi://localhost:1099/StudentService");
            System.out.println(" - rmi://localhost:1099/AdminService");
            System.out.println(" - rmi://localhost:1099/ReportService");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to start RMI server: " + e.getMessage());
        }
    }
}
