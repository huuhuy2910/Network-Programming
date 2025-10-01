package server.dao;

import common.dto.Diem;
import common.dto.HocKy;
import common.dto.HocPhan;
import common.dto.Khoa;
import common.dto.KhoaHoc;
import common.dto.Lop;
import common.dto.NamHoc;
import common.dto.Nganh;
import common.dto.SinhVien;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {

    private static final String BASE_SELECT =
        "SELECT d.diem_id, d.sv_id, d.hp_id, d.hocky_id AS diem_hocky_id, d.namhoc_id AS diem_namhoc_id, d.diem_qua_trinh, d.diem_thi, d.diem_tong_ket, " +
            "sv.ten_sv, sv.ngay_sinh, sv.gioi_tinh, sv.que_quan, sv.dia_chi, sv.sdt, sv.email, sv.anh, sv.status, " +
            "sv.khoahoc_id AS sv_khoahoc_id, svkh.ten_khoahoc AS sv_ten_khoahoc, " +
            "sv.hocky_id AS sv_hocky_id, svhk.ten_hocky AS sv_ten_hocky, " +
            "sv.namhoc_id AS sv_namhoc_id, svnh.ten_namhoc AS sv_ten_namhoc, " +
            "l.lop_id, l.ten_lop, " +
            "n.nganh_id, n.ten_nganh, n.khoa_id, " +
            "k.ten_khoa, " +
            "hp.ten_hp, hp.so_tin_chi, " +
            "hk.ten_hocky AS diem_ten_hocky, nh.ten_namhoc AS diem_ten_namhoc " +
            "FROM diem d " +
            "JOIN sinhvien sv ON d.sv_id = sv.sv_id " +
            "JOIN hocphan hp ON d.hp_id = hp.hp_id " +
            "JOIN lop l ON sv.lop_id = l.lop_id " +
            "JOIN nganh n ON l.nganh_id = n.nganh_id " +
            "JOIN khoa k ON n.khoa_id = k.khoa_id " +
            "LEFT JOIN khoahoc svkh ON sv.khoahoc_id = svkh.khoahoc_id " +
            "LEFT JOIN hocky svhk ON sv.hocky_id = svhk.hocky_id " +
            "LEFT JOIN namhoc svnh ON sv.namhoc_id = svnh.namhoc_id " +
            "LEFT JOIN hocky hk ON d.hocky_id = hk.hocky_id " +
            "LEFT JOIN namhoc nh ON d.namhoc_id = nh.namhoc_id";

    public List<Diem> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY d.diem_id DESC";
        List<Diem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Diem getById(long id) throws SQLException {
        String sql = BASE_SELECT + " WHERE d.diem_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Diem> getBySinhVien(String svId) throws SQLException {
        String sql = BASE_SELECT + " WHERE d.sv_id = ? ORDER BY d.diem_id DESC";
        List<Diem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, svId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean insert(Diem diem) throws SQLException {
        String sql = "INSERT INTO diem (sv_id, hp_id, hocky_id, namhoc_id, diem_qua_trinh, diem_thi, diem_tong_ket) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diem.getSinhVien() != null ? diem.getSinhVien().getSvId() : null);
            ps.setString(2, diem.getHocPhan() != null ? diem.getHocPhan().getMaHocPhan() : null);
            ps.setString(3, diem.getHocKy() != null ? diem.getHocKy().getMaHocKy() : null);
            ps.setString(4, diem.getNamHoc() != null ? diem.getNamHoc().getMaNamHoc() : null);
            ps.setDouble(5, diem.getDiemQuaTrinh());
            ps.setDouble(6, diem.getDiemThi());
            ps.setDouble(7, diem.getDiemTongKet());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Diem diem) throws SQLException {
        String sql = "UPDATE diem SET sv_id = ?, hp_id = ?, hocky_id = ?, namhoc_id = ?, diem_qua_trinh = ?, diem_thi = ?, diem_tong_ket = ? WHERE diem_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diem.getSinhVien() != null ? diem.getSinhVien().getSvId() : null);
            ps.setString(2, diem.getHocPhan() != null ? diem.getHocPhan().getMaHocPhan() : null);
            ps.setString(3, diem.getHocKy() != null ? diem.getHocKy().getMaHocKy() : null);
            ps.setString(4, diem.getNamHoc() != null ? diem.getNamHoc().getMaNamHoc() : null);
            ps.setDouble(5, diem.getDiemQuaTrinh());
            ps.setDouble(6, diem.getDiemThi());
            ps.setDouble(7, diem.getDiemTongKet());
            ps.setLong(8, diem.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM diem WHERE diem_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Diem> search(String keyword) throws SQLException {
    String sql = BASE_SELECT + " WHERE sv.ten_sv LIKE ? OR sv.sv_id LIKE ? OR hp.ten_hp LIKE ? ORDER BY d.diem_id DESC";
        List<Diem> list = new ArrayList<>();
        String like = "%" + keyword + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public double calculateGpa(String svId) throws SQLException {
    String sql = "SELECT SUM(d.diem_tong_ket * hp.so_tin_chi) AS tong_diem, SUM(hp.so_tin_chi) AS tong_tin_chi " +
        "FROM diem d JOIN hocphan hp ON d.hp_id = hp.hp_id WHERE d.sv_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, svId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double tongDiem = rs.getDouble("tong_diem");
                    int tongTinChi = rs.getInt("tong_tin_chi");
                    if (tongTinChi == 0) {
                        return 0.0;
                    }
                    return tongDiem / tongTinChi;
                }
            }
        }
        return 0.0;
    }

    private Diem mapRow(ResultSet rs) throws SQLException {
        Khoa khoa = new Khoa(rs.getString("khoa_id"), rs.getString("ten_khoa"));
        Nganh nganh = new Nganh(rs.getString("nganh_id"), rs.getString("ten_nganh"), khoa);
        Lop lop = new Lop(rs.getString("lop_id"), rs.getString("ten_lop"), nganh, khoa);

        KhoaHoc svKhoaHoc = null;
        String svKhoaHocId = rs.getString("sv_khoahoc_id");
        if (svKhoaHocId != null) {
            svKhoaHoc = new KhoaHoc(svKhoaHocId, rs.getString("sv_ten_khoahoc"));
        }

        SinhVien sinhVien = new SinhVien();
        sinhVien.setSvId(rs.getString("sv_id"));
        sinhVien.setTenSv(rs.getString("ten_sv"));
        Date dob = rs.getDate("ngay_sinh");
        if (dob != null) {
            sinhVien.setNgaySinh(new java.util.Date(dob.getTime()));
        }
        sinhVien.setGioiTinh(rs.getString("gioi_tinh"));
        sinhVien.setQueQuan(rs.getString("que_quan"));
        sinhVien.setDiaChi(rs.getString("dia_chi"));
        sinhVien.setSdt(rs.getString("sdt"));
        sinhVien.setEmail(rs.getString("email"));
        sinhVien.setAnh(rs.getString("anh"));
        sinhVien.setStatus(rs.getString("status"));
        sinhVien.setLop(lop);
        sinhVien.setKhoaHoc(svKhoaHoc);
        HocKy svHocKy = null;
        String svHocKyId = rs.getString("sv_hocky_id");
        if (svHocKyId != null) {
            svHocKy = new HocKy(svHocKyId, rs.getString("sv_ten_hocky"));
        }
        NamHoc svNamHoc = null;
        String svNamHocId = rs.getString("sv_namhoc_id");
        if (svNamHocId != null) {
            svNamHoc = new NamHoc(svNamHocId, rs.getString("sv_ten_namhoc"));
        }
        sinhVien.setHocKyHienTai(svHocKy);
        sinhVien.setNamHocHienTai(svNamHoc);

        HocPhan hocPhan = new HocPhan(rs.getString("hp_id"), rs.getString("ten_hp"), rs.getInt("so_tin_chi"));

        HocKy hocKy = null;
        String hocKyId = rs.getString("diem_hocky_id");
        if (hocKyId != null) {
            hocKy = new HocKy(hocKyId, rs.getString("diem_ten_hocky"));
        }

        NamHoc namHoc = null;
        String namHocId = rs.getString("diem_namhoc_id");
        if (namHocId != null) {
            namHoc = new NamHoc(namHocId, rs.getString("diem_ten_namhoc"));
        }

        Diem diem = new Diem();
        diem.setId(rs.getLong("diem_id"));
        diem.setSinhVien(sinhVien);
        diem.setHocPhan(hocPhan);
        diem.setHocKy(hocKy);
        diem.setNamHoc(namHoc);
        diem.setDiemQuaTrinh(rs.getDouble("diem_qua_trinh"));
        diem.setDiemThi(rs.getDouble("diem_thi"));
        diem.setDiemTongKet(rs.getDouble("diem_tong_ket"));
        return diem;
    }
}
