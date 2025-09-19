package vn.edu.app.server.dao.impl;

import vn.edu.app.common.dto.CourseDTO;
import vn.edu.app.server.dao.CourseDAO;
import vn.edu.app.server.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {
    @Override
    public boolean insert(CourseDTO c) {
        String sql = "INSERT INTO courses (course_id, course_name, credits, semester) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getSemester());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean update(CourseDTO c) {
        String sql = "UPDATE courses SET course_name=?, credits=?, semester=? WHERE course_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getCredits());
            ps.setString(3, c.getSemester());
            ps.setString(4, c.getCourseId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<CourseDTO> findAll() {
        String sql = "SELECT course_id, course_name, credits, semester FROM courses";
        List<CourseDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new CourseDTO(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("semester")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<CourseDTO> findById(String courseId) {
        String sql = "SELECT course_id, course_name, credits, semester FROM courses WHERE course_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CourseDTO(
                            rs.getString("course_id"),
                            rs.getString("course_name"),
                            rs.getInt("credits"),
                            rs.getString("semester")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
}
