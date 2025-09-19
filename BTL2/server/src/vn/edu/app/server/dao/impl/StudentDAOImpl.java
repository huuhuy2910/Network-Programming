package vn.edu.app.server.dao.impl;

import vn.edu.app.common.dto.StudentDTO;
import vn.edu.app.server.dao.StudentDAO;
import vn.edu.app.server.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public Optional<StudentDTO> findById(String studentId) {
        String sql = "SELECT student_id, full_name, gender, class_id, phone, email, address, hometown, status " +
                     "FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean insert(StudentDTO s) {
        String sql = "INSERT INTO students (student_id, full_name, gender, class_id, phone, email, address, hometown, status) " +
                     "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bind(ps, s);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean update(StudentDTO s) {
        String sql = "UPDATE students SET full_name=?, gender=?, class_id=?, phone=?, email=?, address=?, hometown=?, status=? " +
                     "WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getGender());
            ps.setString(3, s.getClassId());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getAddress());
            ps.setString(7, s.getHometown());
            ps.setString(8, s.getStatus());
            ps.setString(9, s.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(String studentId) {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<StudentDTO> findAll() {
        String sql = "SELECT student_id, full_name, gender, class_id, phone, email, address, hometown, status FROM students";
        List<StudentDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private static StudentDTO map(ResultSet rs) throws SQLException {
        return new StudentDTO(
                rs.getString("student_id"),
                rs.getString("full_name"),
                rs.getString("gender"),
                rs.getString("class_id"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("hometown"),
                rs.getString("status")
        );
    }

    private static void bind(PreparedStatement ps, StudentDTO s) throws SQLException {
        ps.setString(1, s.getStudentId());
        ps.setString(2, s.getFullName());
        ps.setString(3, s.getGender());
        ps.setString(4, s.getClassId());
        ps.setString(5, s.getPhone());
        ps.setString(6, s.getEmail());
        ps.setString(7, s.getAddress());
        ps.setString(8, s.getHometown());
        ps.setString(9, s.getStatus());
    }
}
