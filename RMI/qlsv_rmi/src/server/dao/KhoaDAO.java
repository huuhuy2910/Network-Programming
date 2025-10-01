package server.dao;

import common.dto.Khoa;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoaDAO {

    public List<Khoa> getAll() throws SQLException {
        String sql = "SELECT khoa_id, ten_khoa FROM khoa ORDER BY ten_khoa";
        List<Khoa> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Khoa getById(String id) throws SQLException {
        String sql = "SELECT khoa_id, ten_khoa FROM khoa WHERE khoa_id = ?";
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

    public boolean insert(Khoa khoa) throws SQLException {
        String sql = "INSERT INTO khoa (khoa_id, ten_khoa) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, khoa.getMaKhoa());
            ps.setString(2, khoa.getTenKhoa());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Khoa khoa) throws SQLException {
        String sql = "UPDATE khoa SET ten_khoa = ? WHERE khoa_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, khoa.getTenKhoa());
            ps.setString(2, khoa.getMaKhoa());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM khoa WHERE khoa_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Khoa> search(String keyword) throws SQLException {
        String sql = "SELECT khoa_id, ten_khoa FROM khoa WHERE ten_khoa LIKE ? OR khoa_id LIKE ? ORDER BY ten_khoa";
        List<Khoa> list = new ArrayList<>();
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

    private Khoa mapRow(ResultSet rs) throws SQLException {
        Khoa khoa = new Khoa();
        khoa.setMaKhoa(rs.getString("khoa_id"));
        khoa.setTenKhoa(rs.getString("ten_khoa"));
        return khoa;
    }
}
