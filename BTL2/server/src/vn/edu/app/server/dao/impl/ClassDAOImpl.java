package vn.edu.app.server.dao.impl;

import vn.edu.app.common.dto.ClassDTO;
import vn.edu.app.server.dao.ClassDAO;
import vn.edu.app.server.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAOImpl implements ClassDAO {
    @Override
    public List<ClassDTO> findAll() {
        String sql = "SELECT class_id, class_name, course, major_id FROM classes";
        List<ClassDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ClassDTO(
                        rs.getString("class_id"),
                        rs.getString("class_name"),
                        rs.getString("course"),
                        rs.getString("major_id")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean exists(String classId) {
        String sql = "SELECT 1 FROM classes WHERE class_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
