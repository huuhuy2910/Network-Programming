package server.dao;

import common.dto.Khoa;
import common.dto.Lop;
import common.dto.Nganh;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LopDAO {

    private static final String BASE_SELECT = "SELECT l.lop_id, l.ten_lop, l.nganh_id, " +
        "n.ten_nganh, n.khoa_id, k.ten_khoa " +
        "FROM lop l " +
        "JOIN nganh n ON l.nganh_id = n.nganh_id " +
        "JOIN khoa k ON n.khoa_id = k.khoa_id";

    public List<Lop> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY l.ten_lop";
        List<Lop> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Lop getById(String id) throws SQLException {
        String sql = BASE_SELECT + " WHERE l.lop_id = ?";
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

    public boolean insert(Lop lop) throws SQLException {
        String sql = "INSERT INTO lop (lop_id, ten_lop, nganh_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getMaLop());
            ps.setString(2, lop.getTenLop());
            ps.setString(3, lop.getNganh() != null ? lop.getNganh().getMaNganh() : null);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Lop lop) throws SQLException {
        String sql = "UPDATE lop SET ten_lop = ?, nganh_id = ? WHERE lop_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getTenLop());
            ps.setString(2, lop.getNganh() != null ? lop.getNganh().getMaNganh() : null);
            ps.setString(3, lop.getMaLop());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM lop WHERE lop_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Lop> search(String keyword) throws SQLException {
        String sql = BASE_SELECT + " WHERE l.ten_lop LIKE ? OR l.lop_id LIKE ? ORDER BY l.ten_lop";
        List<Lop> list = new ArrayList<>();
        String like = "%" + keyword + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, like);
                ps.setString(2, like);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                }
        }
        return list;
    }

    private Lop mapRow(ResultSet rs) throws SQLException {
    Khoa khoa = new Khoa(rs.getString("khoa_id"), rs.getString("ten_khoa"));
    Nganh nganh = new Nganh(rs.getString("nganh_id"), rs.getString("ten_nganh"), khoa);
        Lop lop = new Lop();
        lop.setMaLop(rs.getString("lop_id"));
        lop.setTenLop(rs.getString("ten_lop"));
        lop.setNganh(nganh);
        lop.setKhoa(khoa);
        return lop;
    }
}
