package vn.edu.app.server.dao.impl;

import vn.edu.app.common.dto.MajorDTO;
import vn.edu.app.server.dao.MajorDAO;
import vn.edu.app.server.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MajorDAOImpl implements MajorDAO {
    @Override
    public List<MajorDTO> findAll() {
        String sql = "SELECT major_id, major_name FROM majors";
        List<MajorDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MajorDTO(
                        rs.getString("major_id"),
                        rs.getString("major_name")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean exists(String majorId) {
        String sql = "SELECT 1 FROM majors WHERE major_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, majorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
