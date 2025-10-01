package client.network;

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

import java.rmi.Naming;

public final class ClientConnector {
    private static final String HOST = "localhost";
    private static final int PORT = 1099;

    private ClientConnector() {
    }

    private static String buildUrl(String serviceName) {
        return String.format("rmi://%s:%d/%s", HOST, PORT, serviceName);
    }

    public static SinhVienService getSinhVienService() throws Exception {
        return (SinhVienService) Naming.lookup(buildUrl("SinhVienService"));
    }

    public static LopService getLopService() throws Exception {
        return (LopService) Naming.lookup(buildUrl("LopService"));
    }

    public static NganhService getNganhService() throws Exception {
        return (NganhService) Naming.lookup(buildUrl("NganhService"));
    }

    public static KhoaService getKhoaService() throws Exception {
        return (KhoaService) Naming.lookup(buildUrl("KhoaService"));
    }

    public static HocPhanService getHocPhanService() throws Exception {
        return (HocPhanService) Naming.lookup(buildUrl("HocPhanService"));
    }

    public static DiemService getDiemService() throws Exception {
        return (DiemService) Naming.lookup(buildUrl("DiemService"));
    }

    public static TaiKhoanService getTaiKhoanService() throws Exception {
        return (TaiKhoanService) Naming.lookup(buildUrl("TaiKhoanService"));
    }

    public static KhoaHocService getKhoaHocService() throws Exception {
        return (KhoaHocService) Naming.lookup(buildUrl("KhoaHocService"));
    }

    public static HocKyService getHocKyService() throws Exception {
        return (HocKyService) Naming.lookup(buildUrl("HocKyService"));
    }

    public static NamHocService getNamHocService() throws Exception {
        return (NamHocService) Naming.lookup(buildUrl("NamHocService"));
    }
}
