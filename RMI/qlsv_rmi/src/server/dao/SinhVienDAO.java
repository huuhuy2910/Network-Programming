package server.dao;

import common.dto.DashboardClassSummary;
import common.dto.DashboardStats;
import common.dto.HocKy;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SinhVienDAO {

    private static final String BASE_SELECT =
        "SELECT sv.sv_id, sv.ten_sv, sv.ngay_sinh, sv.gioi_tinh, sv.que_quan, sv.dia_chi, sv.sdt, sv.email, sv.anh, sv.status, " +
            "sv.khoahoc_id AS sv_khoahoc_id, kh.ten_khoahoc, " +
            "sv.hocky_id AS sv_hocky_id, hk.ten_hocky, " +
            "sv.namhoc_id AS sv_namhoc_id, nh.ten_namhoc, " +
            "l.lop_id, l.ten_lop, " +
            "n.nganh_id, n.ten_nganh, n.khoa_id, " +
            "k.ten_khoa " +
            "FROM sinhvien sv " +
            "JOIN lop l ON sv.lop_id = l.lop_id " +
            "JOIN nganh n ON l.nganh_id = n.nganh_id " +
            "JOIN khoa k ON n.khoa_id = k.khoa_id " +
            "JOIN khoahoc kh ON sv.khoahoc_id = kh.khoahoc_id " +
            "JOIN hocky hk ON sv.hocky_id = hk.hocky_id " +
            "JOIN namhoc nh ON sv.namhoc_id = nh.namhoc_id";

    private static final String DASHBOARD_BASE_JOIN =
        " FROM sinhvien sv " +
            "JOIN lop l ON sv.lop_id = l.lop_id " +
            "JOIN nganh n ON l.nganh_id = n.nganh_id " +
            "JOIN khoa k ON n.khoa_id = k.khoa_id " +
            "LEFT JOIN khoahoc kh ON sv.khoahoc_id = kh.khoahoc_id " +
            "LEFT JOIN namhoc nh ON sv.namhoc_id = nh.namhoc_id";

    public List<SinhVien> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY sv.ten_sv";
        List<SinhVien> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public SinhVien getById(String id) throws SQLException {
        String sql = BASE_SELECT + " WHERE sv.sv_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean insert(SinhVien sv) throws SQLException {
        String sql = "INSERT INTO sinhvien (sv_id, ten_sv, ngay_sinh, gioi_tinh, que_quan, dia_chi, sdt, email, anh, lop_id, status, khoahoc_id, hocky_id, namhoc_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getSvId());
            ps.setString(2, sv.getTenSv());
            ps.setDate(3, sv.getNgaySinh() != null ? new Date(sv.getNgaySinh().getTime()) : null);
            ps.setString(4, sv.getGioiTinh());
            ps.setString(5, sv.getQueQuan());
            ps.setString(6, sv.getDiaChi());
            ps.setString(7, sv.getSdt());
            ps.setString(8, sv.getEmail());
            ps.setString(9, sv.getAnh());
            ps.setString(10, sv.getLop() != null ? sv.getLop().getMaLop() : null);
            ps.setString(11, sv.getStatus());
            ps.setString(12, sv.getKhoaHoc() != null ? sv.getKhoaHoc().getMaKhoaHoc() : null);
            ps.setString(13, sv.getHocKyHienTai() != null ? sv.getHocKyHienTai().getMaHocKy() : null);
            ps.setString(14, sv.getNamHocHienTai() != null ? sv.getNamHocHienTai().getMaNamHoc() : null);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(SinhVien sv) throws SQLException {
        String sql = "UPDATE sinhvien SET ten_sv = ?, ngay_sinh = ?, gioi_tinh = ?, que_quan = ?, dia_chi = ?, sdt = ?, email = ?, anh = ?, lop_id = ?, status = ?, khoahoc_id = ?, hocky_id = ?, namhoc_id = ? WHERE sv_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sv.getTenSv());
            ps.setDate(2, sv.getNgaySinh() != null ? new Date(sv.getNgaySinh().getTime()) : null);
            ps.setString(3, sv.getGioiTinh());
            ps.setString(4, sv.getQueQuan());
            ps.setString(5, sv.getDiaChi());
            ps.setString(6, sv.getSdt());
            ps.setString(7, sv.getEmail());
            ps.setString(8, sv.getAnh());
            ps.setString(9, sv.getLop() != null ? sv.getLop().getMaLop() : null);
            ps.setString(10, sv.getStatus());
            ps.setString(11, sv.getKhoaHoc() != null ? sv.getKhoaHoc().getMaKhoaHoc() : null);
            ps.setString(12, sv.getHocKyHienTai() != null ? sv.getHocKyHienTai().getMaHocKy() : null);
            ps.setString(13, sv.getNamHocHienTai() != null ? sv.getNamHocHienTai().getMaNamHoc() : null);
            ps.setString(14, sv.getSvId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM sinhvien WHERE sv_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<SinhVien> search(String keyword) throws SQLException {
    String sql = BASE_SELECT + " WHERE sv.ten_sv LIKE ? OR sv.sv_id LIKE ? OR l.ten_lop LIKE ? ORDER BY sv.ten_sv";
        List<SinhVien> list = new ArrayList<>();
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

    private SinhVien mapRow(ResultSet rs) throws SQLException {
        Khoa khoa = new Khoa(rs.getString("khoa_id"), rs.getString("ten_khoa"));
        Nganh nganh = new Nganh(rs.getString("nganh_id"), rs.getString("ten_nganh"), khoa);
        Lop lop = new Lop(rs.getString("lop_id"), rs.getString("ten_lop"), nganh, khoa);

        KhoaHoc khoaHoc = null;
        String khoaHocId = rs.getString("sv_khoahoc_id");
        if (khoaHocId != null) {
            khoaHoc = new KhoaHoc(khoaHocId, rs.getString("ten_khoahoc"));
        }

        HocKy hocKy = null;
        String hocKyId = rs.getString("sv_hocky_id");
        if (hocKyId != null) {
            hocKy = new HocKy(hocKyId, rs.getString("ten_hocky"));
        }

        NamHoc namHoc = null;
        String namHocId = rs.getString("sv_namhoc_id");
        if (namHocId != null) {
            namHoc = new NamHoc(namHocId, rs.getString("ten_namhoc"));
        }

        SinhVien sv = new SinhVien();
        sv.setSvId(rs.getString("sv_id"));
        sv.setTenSv(rs.getString("ten_sv"));
        Date dob = rs.getDate("ngay_sinh");
        if (dob != null) {
            sv.setNgaySinh(new java.util.Date(dob.getTime()));
        }
        sv.setGioiTinh(rs.getString("gioi_tinh"));
        sv.setQueQuan(rs.getString("que_quan"));
        sv.setDiaChi(rs.getString("dia_chi"));
        sv.setSdt(rs.getString("sdt"));
        sv.setEmail(rs.getString("email"));
        sv.setAnh(rs.getString("anh"));
        sv.setStatus(rs.getString("status"));
        sv.setLop(lop);
        sv.setKhoaHoc(khoaHoc);
        sv.setHocKyHienTai(hocKy);
        sv.setNamHocHienTai(namHoc);
        return sv;
    }

    public DashboardStats getDashboardStats(String khoaId, String nganhId, String khoaHocId) throws SQLException {
        DashboardStats stats = new DashboardStats();
        FilterParams filters = buildFilterParams(khoaId, nganhId, khoaHocId);

        stats.getEnrollmentByYear().putAll(fetchEnrollmentByYear(filters));
        stats.getGenderDistribution().putAll(fetchGenderDistribution(filters));
        stats.getStatusBreakdown().putAll(fetchStatusBreakdown(filters));
        stats.getTopClasses().addAll(fetchTopClasses(filters));
        stats.setGeneratedAt(new java.util.Date());
        return stats;
    }

    private Map<String, Integer> fetchEnrollmentByYear(FilterParams filters) throws SQLException {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT COALESCE(nh.ten_namhoc, 'Không xác định') AS label, COUNT(*) AS total" +
            DASHBOARD_BASE_JOIN +
            filters.whereClause +
            " GROUP BY COALESCE(nh.ten_namhoc, 'Không xác định') ORDER BY label";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            filters.apply(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("label"), rs.getInt("total"));
                }
            }
        }
        return result;
    }

    private Map<String, Integer> fetchGenderDistribution(FilterParams filters) throws SQLException {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT COALESCE(sv.gioi_tinh, 'Khác') AS label, COUNT(*) AS total" +
            DASHBOARD_BASE_JOIN +
            filters.whereClause +
            " GROUP BY COALESCE(sv.gioi_tinh, 'Khác') ORDER BY label";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            filters.apply(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("label"), rs.getInt("total"));
                }
            }
        }
        return result;
    }

    private Map<String, Integer> fetchStatusBreakdown(FilterParams filters) throws SQLException {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT COALESCE(sv.status, 'Không xác định') AS label, COUNT(*) AS total" +
            DASHBOARD_BASE_JOIN +
            filters.whereClause +
            " GROUP BY COALESCE(sv.status, 'Không xác định') ORDER BY label";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            filters.apply(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("label"), rs.getInt("total"));
                }
            }
        }
        return result;
    }

    private List<DashboardClassSummary> fetchTopClasses(FilterParams filters) throws SQLException {
        List<DashboardClassSummary> result = new ArrayList<>();
        String sql = "SELECT l.lop_id, l.ten_lop, COUNT(*) AS total" +
            DASHBOARD_BASE_JOIN +
            filters.whereClause +
            " GROUP BY l.lop_id, l.ten_lop ORDER BY total DESC, l.ten_lop ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            filters.apply(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DashboardClassSummary summary = new DashboardClassSummary(
                        rs.getString("lop_id"),
                        rs.getString("ten_lop"),
                        null,
                        rs.getInt("total"));
                    result.add(summary);
                    if (result.size() >= 8) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    private FilterParams buildFilterParams(String khoaId, String nganhId, String khoaHocId) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (khoaId != null && !khoaId.isBlank()) {
            where.append(" AND k.khoa_id = ?");
            params.add(khoaId);
        }
        if (nganhId != null && !nganhId.isBlank()) {
            where.append(" AND n.nganh_id = ?");
            params.add(nganhId);
        }
        if (khoaHocId != null && !khoaHocId.isBlank()) {
            where.append(" AND sv.khoahoc_id = ?");
            params.add(khoaHocId);
        }
        return new FilterParams(where.toString(), params);
    }

    private static class FilterParams {
        private final String whereClause;
        private final List<Object> params;

        private FilterParams(String whereClause, List<Object> params) {
            this.whereClause = whereClause;
            this.params = params;
        }

        private void apply(PreparedStatement ps) throws SQLException {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        }
    }
}
