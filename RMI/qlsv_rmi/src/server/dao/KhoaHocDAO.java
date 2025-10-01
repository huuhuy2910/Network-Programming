package server.dao;

import common.dto.KhoaHoc;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocDAO {

    private static final String BASE_SELECT = "SELECT khoahoc_id, ten_khoahoc FROM khoahoc";

    public List<KhoaHoc> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY ten_khoahoc";
        List<KhoaHoc> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public KhoaHoc getById(String id) throws SQLException {
        String sql = BASE_SELECT + " WHERE khoahoc_id = ?";
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

    public boolean insert(KhoaHoc item) throws SQLException {
        String sql = "INSERT INTO khoahoc (khoahoc_id, ten_khoahoc) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getMaKhoaHoc());
            ps.setString(2, item.getTenKhoaHoc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(KhoaHoc item) throws SQLException {
        String sql = "UPDATE khoahoc SET ten_khoahoc = ? WHERE khoahoc_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTenKhoaHoc());
            ps.setString(2, item.getMaKhoaHoc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM khoahoc WHERE khoahoc_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<KhoaHoc> search(String keyword) throws SQLException {
        String sql = BASE_SELECT + " WHERE ten_khoahoc LIKE ? OR khoahoc_id LIKE ? ORDER BY ten_khoahoc";
        List<KhoaHoc> list = new ArrayList<>();
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

    private KhoaHoc mapRow(ResultSet rs) throws SQLException {
        return new KhoaHoc(rs.getString("khoahoc_id"), rs.getString("ten_khoahoc"));
    }
}
