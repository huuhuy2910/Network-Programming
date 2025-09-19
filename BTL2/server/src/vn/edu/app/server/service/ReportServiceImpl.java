package vn.edu.app.server.service;

import vn.edu.app.common.remote.ReportService;
import vn.edu.app.server.util.DBConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportServiceImpl extends UnicastRemoteObject implements ReportService {

    public ReportServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public Map<String, Integer> countClassesByMajor() throws RemoteException {
        String sql = "SELECT m.major_name, COUNT(c.class_id) AS cnt " +
                     "FROM majors m LEFT JOIN classes c ON m.major_id = c.major_id " +
                     "GROUP BY m.major_id, m.major_name ORDER BY m.major_name";
        return runCountQuery(sql, "major_name", "cnt");
    }

    @Override
    public Map<String, Integer> countStudentsByMajor() throws RemoteException {
        String sql = "SELECT m.major_name, COUNT(s.student_id) AS cnt " +
                     "FROM majors m " +
                     "LEFT JOIN classes c ON m.major_id = c.major_id " +
                     "LEFT JOIN students s ON c.class_id = s.class_id " +
                     "GROUP BY m.major_id, m.major_name ORDER BY m.major_name";
        return runCountQuery(sql, "major_name", "cnt");
    }

    @Override
    public Map<String, Integer> countStudentsByStatus() throws RemoteException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM students GROUP BY status ORDER BY status";
        return runCountQuery(sql, "status", "cnt");
    }

    private Map<String, Integer> runCountQuery(String sql, String keyCol, String valCol) {
        Map<String, Integer> map = new LinkedHashMap<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(keyCol), rs.getInt(valCol));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
}
