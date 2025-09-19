package vn.edu.app.server.dao.impl;

import vn.edu.app.common.dto.GradeDTO;
import vn.edu.app.server.dao.GradeDAO;
import vn.edu.app.server.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAOImpl implements GradeDAO {
    @Override
    public boolean upsert(GradeDTO g) {
        String sql = """
            INSERT INTO grades (student_id, course_id, score, grade_note)
            VALUES (?,?,?,?)
            ON DUPLICATE KEY UPDATE
              score = VALUES(score),
              grade_note = VALUES(grade_note),
              updated_at = CURRENT_TIMESTAMP
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, g.getStudentId());
            ps.setString(2, g.getCourseId());
            ps.setFloat(3, g.getScore());
            ps.setString(4, g.getGradeNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<GradeDTO> findByStudent(String studentId) {
        String sql = "SELECT id, student_id, course_id, score, grade_note FROM grades WHERE student_id=?";
        List<GradeDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new GradeDTO(
                            rs.getInt("id"),
                            rs.getString("student_id"),
                            rs.getString("course_id"),
                            rs.getFloat("score"),
                            rs.getString("grade_note")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
