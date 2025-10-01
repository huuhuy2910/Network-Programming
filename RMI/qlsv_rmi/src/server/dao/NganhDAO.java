package server.dao;

import common.dto.Khoa;
import common.dto.Nganh;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NganhDAO {

    private static final String BASE_SELECT = "SELECT n.nganh_id, n.ten_nganh, n.khoa_id, " +
        "k.ten_khoa FROM nganh n " +
        "JOIN khoa k ON n.khoa_id = k.khoa_id";

    public List<Nganh> getAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY n.ten_nganh";
        List<Nganh> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Nganh getById(String id) throws SQLException {
        String sql = BASE_SELECT + " WHERE n.nganh_id = ?";
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

    public boolean insert(Nganh nganh) throws SQLException {
        String sql = "INSERT INTO nganh (nganh_id, ten_nganh, khoa_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nganh.getMaNganh());
            ps.setString(2, nganh.getTenNganh());
            ps.setString(3, nganh.getKhoa() != null ? nganh.getKhoa().getMaKhoa() : null);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Nganh nganh) throws SQLException {
        String sql = "UPDATE nganh SET ten_nganh = ?, khoa_id = ? WHERE nganh_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nganh.getTenNganh());
            ps.setString(2, nganh.getKhoa() != null ? nganh.getKhoa().getMaKhoa() : null);
            ps.setString(3, nganh.getMaNganh());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM nganh WHERE nganh_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Nganh> search(String keyword) throws SQLException {
        String sql = BASE_SELECT + " WHERE n.ten_nganh LIKE ? OR n.nganh_id LIKE ? ORDER BY n.ten_nganh";
        List<Nganh> list = new ArrayList<>();
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

    private Nganh mapRow(ResultSet rs) throws SQLException {
        Khoa khoa = new Khoa();
        khoa.setMaKhoa(rs.getString("khoa_id"));
        khoa.setTenKhoa(rs.getString("ten_khoa"));

        Nganh nganh = new Nganh();
        nganh.setMaNganh(rs.getString("nganh_id"));
        nganh.setTenNganh(rs.getString("ten_nganh"));
        nganh.setKhoa(khoa);
        return nganh;
    }
}
