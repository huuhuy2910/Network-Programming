package server.dao;

import common.dto.NamHoc;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NamHocDAO {

    private static final String BASE_SELECT = "SELECT namhoc_id, ten_namhoc FROM namhoc";

    public List<NamHoc> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY ten_namhoc";
        List<NamHoc> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public NamHoc getById(String id) throws SQLException {
        String sql = BASE_SELECT + " WHERE namhoc_id = ?";
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

    public boolean insert(NamHoc item) throws SQLException {
        String sql = "INSERT INTO namhoc (namhoc_id, ten_namhoc) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getMaNamHoc());
            ps.setString(2, item.getTenNamHoc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(NamHoc item) throws SQLException {
        String sql = "UPDATE namhoc SET ten_namhoc = ? WHERE namhoc_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTenNamHoc());
            ps.setString(2, item.getMaNamHoc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM namhoc WHERE namhoc_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<NamHoc> search(String keyword) throws SQLException {
        String sql = BASE_SELECT + " WHERE ten_namhoc LIKE ? OR namhoc_id LIKE ? ORDER BY ten_namhoc";
        List<NamHoc> list = new ArrayList<>();
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

    private NamHoc mapRow(ResultSet rs) throws SQLException {
        return new NamHoc(rs.getString("namhoc_id"), rs.getString("ten_namhoc"));
    }
}
