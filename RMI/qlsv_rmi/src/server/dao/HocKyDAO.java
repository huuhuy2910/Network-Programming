package server.dao;

import common.dto.HocKy;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HocKyDAO {

    private static final String BASE_SELECT = "SELECT hocky_id, ten_hocky FROM hocky";

    public List<HocKy> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY ten_hocky";
        List<HocKy> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public HocKy getById(String id) throws SQLException {
        String sql = BASE_SELECT + " WHERE hocky_id = ?";
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

    public boolean insert(HocKy item) throws SQLException {
        String sql = "INSERT INTO hocky (hocky_id, ten_hocky) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getMaHocKy());
            ps.setString(2, item.getTenHocKy());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(HocKy item) throws SQLException {
        String sql = "UPDATE hocky SET ten_hocky = ? WHERE hocky_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTenHocKy());
            ps.setString(2, item.getMaHocKy());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM hocky WHERE hocky_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<HocKy> search(String keyword) throws SQLException {
        String sql = BASE_SELECT + " WHERE ten_hocky LIKE ? OR hocky_id LIKE ? ORDER BY ten_hocky";
        List<HocKy> list = new ArrayList<>();
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

    private HocKy mapRow(ResultSet rs) throws SQLException {
        return new HocKy(rs.getString("hocky_id"), rs.getString("ten_hocky"));
    }
}
