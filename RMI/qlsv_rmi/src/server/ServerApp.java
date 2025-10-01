package server;

import common.service.DiemService;
import common.service.HocKyService;
import common.service.HocPhanService;
import common.service.KhoaHocService;
import common.service.KhoaService;
import common.service.LopService;
import common.service.NamHocService;
import common.service.NganhService;
import common.service.SinhVienService;
import common.service.TaiKhoanService;
import server.serviceimpl.DiemServiceImpl;
import server.serviceimpl.HocKyServiceImpl;
import server.serviceimpl.HocPhanServiceImpl;
import server.serviceimpl.KhoaHocServiceImpl;
import server.serviceimpl.KhoaServiceImpl;
import server.serviceimpl.LopServiceImpl;
import server.serviceimpl.NamHocServiceImpl;
import server.serviceimpl.NganhServiceImpl;
import server.serviceimpl.SinhVienServiceImpl;
import server.serviceimpl.TaiKhoanServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerApp {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            SinhVienService sinhVienService = new SinhVienServiceImpl();
            LopService lopService = new LopServiceImpl();
            NganhService nganhService = new NganhServiceImpl();
            KhoaService khoaService = new KhoaServiceImpl();
            KhoaHocService khoaHocService = new KhoaHocServiceImpl();
            HocKyService hocKyService = new HocKyServiceImpl();
            NamHocService namHocService = new NamHocServiceImpl();
            HocPhanService hocPhanService = new HocPhanServiceImpl();
            DiemService diemService = new DiemServiceImpl();
            TaiKhoanService taiKhoanService = new TaiKhoanServiceImpl();

            Naming.rebind("rmi://localhost:1099/SinhVienService", sinhVienService);
            Naming.rebind("rmi://localhost:1099/LopService", lopService);
            Naming.rebind("rmi://localhost:1099/NganhService", nganhService);
            Naming.rebind("rmi://localhost:1099/KhoaService", khoaService);
            Naming.rebind("rmi://localhost:1099/KhoaHocService", khoaHocService);
            Naming.rebind("rmi://localhost:1099/HocKyService", hocKyService);
            Naming.rebind("rmi://localhost:1099/NamHocService", namHocService);
            Naming.rebind("rmi://localhost:1099/HocPhanService", hocPhanService);
            Naming.rebind("rmi://localhost:1099/DiemService", diemService);
            Naming.rebind("rmi://localhost:1099/TaiKhoanService", taiKhoanService);

            System.out.println("Server RMI is running on port 1099...");
        } catch (Exception e) {
            System.err.println("Failed to start RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
