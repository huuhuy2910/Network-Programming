package server.dao;

import common.dto.HocPhan;
import server.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HocPhanDAO {

    public List<HocPhan> getAll() throws SQLException {
        String sql = "SELECT hp_id, ten_hp, so_tin_chi FROM hocphan ORDER BY ten_hp";
        List<HocPhan> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public HocPhan getById(String id) throws SQLException {
        String sql = "SELECT hp_id, ten_hp, so_tin_chi FROM hocphan WHERE hp_id = ?";
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

    public boolean insert(HocPhan hocPhan) throws SQLException {
        String sql = "INSERT INTO hocphan (hp_id, ten_hp, so_tin_chi) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hocPhan.getMaHocPhan());
            ps.setString(2, hocPhan.getTenHocPhan());
            ps.setInt(3, hocPhan.getSoTinChi());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(HocPhan hocPhan) throws SQLException {
        String sql = "UPDATE hocphan SET ten_hp = ?, so_tin_chi = ? WHERE hp_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hocPhan.getTenHocPhan());
            ps.setInt(2, hocPhan.getSoTinChi());
            ps.setString(3, hocPhan.getMaHocPhan());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM hocphan WHERE hp_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<HocPhan> search(String keyword) throws SQLException {
        String sql = "SELECT hp_id, ten_hp, so_tin_chi FROM hocphan WHERE ten_hp LIKE ? OR hp_id LIKE ? ORDER BY ten_hp";
        List<HocPhan> list = new ArrayList<>();
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

    private HocPhan mapRow(ResultSet rs) throws SQLException {
        HocPhan hocPhan = new HocPhan();
        hocPhan.setMaHocPhan(rs.getString("hp_id"));
        hocPhan.setTenHocPhan(rs.getString("ten_hp"));
        hocPhan.setSoTinChi(rs.getInt("so_tin_chi"));
        return hocPhan;
    }
}
