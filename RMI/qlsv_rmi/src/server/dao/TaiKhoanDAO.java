package server.dao;

import common.dto.TaiKhoan;
import server.util.DBConnection;
import server.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public List<TaiKhoan> getAll() throws SQLException {
        String sql = buildSelectSql() + " ORDER BY tk.username";
        return executeListQuery(sql, ps -> {});
    }

    public TaiKhoan getByUsername(String username) throws SQLException {
        String sql = buildSelectSql() + " WHERE tk.username = ?";
        return executeSingleQuery(sql, ps -> ps.setString(1, username));
    }

    public boolean insert(TaiKhoan tk) throws SQLException {
        String sql = "INSERT INTO taikhoan (username, password, role, sv_id, status, ngay_tao) VALUES (?, ?, ?, ?, ?, ?)";
        String hashedPassword = PasswordUtil.hashPassword(tk.getPassword());
        Timestamp createdAt = tk.getCreatedAt() != null ? new Timestamp(tk.getCreatedAt().getTime()) : new Timestamp(System.currentTimeMillis());
        return executeUpdate(sql, ps -> {
            ps.setString(1, tk.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, tk.getRole());
            ps.setString(4, tk.getSvId());
            ps.setString(5, tk.getStatus());
            ps.setTimestamp(6, createdAt);
        });
    }

    public boolean update(TaiKhoan tk) throws SQLException {
        String sql = "UPDATE taikhoan SET role = ?, sv_id = ?, status = ? WHERE username = ?";
        return executeUpdate(sql, ps -> {
            ps.setString(1, tk.getRole());
            ps.setString(2, tk.getSvId());
            ps.setString(3, tk.getStatus());
            ps.setString(4, tk.getUsername());
        });
    }

    public boolean delete(String username) throws SQLException {
        String sql = "DELETE FROM taikhoan WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        }
    }

    public List<TaiKhoan> search(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = buildSelectSql() + " WHERE tk.username LIKE ? OR tk.sv_id LIKE ? ORDER BY tk.username";
        return executeListQuery(sql, ps -> {
            ps.setString(1, like);
            ps.setString(2, like);
        });
    }

    public TaiKhoan login(String username, String password) throws SQLException {
        TaiKhoan taiKhoan = getByUsername(username);
        if (taiKhoan == null) {
            return null;
        }
        String storedPassword = taiKhoan.getPassword();
        if (storedPassword == null) {
            return null;
        }
        if (PasswordUtil.matches(password, storedPassword) || storedPassword.equals(password)) {
            return taiKhoan;
        }
        return null;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        TaiKhoan taiKhoan = login(username, oldPassword);
        if (taiKhoan == null) {
            return false;
        }
        return resetPassword(username, newPassword);
    }

    public boolean resetPassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE taikhoan SET password = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hashPassword(newPassword));
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        }
    }

    private TaiKhoan mapRow(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setUsername(rs.getString("username"));
        tk.setPassword(rs.getString("password"));
        tk.setRole(rs.getString("role"));
        tk.setSvId(rs.getString("sv_id"));
        String studentName = rs.getString("student_name");
        tk.setDisplayName(studentName != null && !studentName.isBlank() ? studentName : tk.getUsername());
        tk.setStatus(rs.getString("status"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            tk.setCreatedAt(new java.util.Date(created.getTime()));
        }
        return tk;
    }

    private List<TaiKhoan> executeListQuery(String sql, StatementConfigurer configurer) throws SQLException {
        List<TaiKhoan> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (configurer != null) {
                configurer.accept(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private TaiKhoan executeSingleQuery(String sql, StatementConfigurer configurer) throws SQLException {
        List<TaiKhoan> list = executeListQuery(sql, configurer);
        return list.isEmpty() ? null : list.get(0);
    }

    private boolean executeUpdate(String sql, StatementConfigurer configurer) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            configurer.accept(ps);
            return ps.executeUpdate() > 0;
        }
    }

    private String buildSelectSql() {
        return "SELECT tk.username, tk.password, tk.role, tk.sv_id, tk.status, tk.ngay_tao AS created_at, sv.ten_sv AS student_name " +
            "FROM taikhoan tk LEFT JOIN sinhvien sv ON tk.sv_id = sv.sv_id";
    }

    @FunctionalInterface
    private interface StatementConfigurer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}
