package vn.edu.app.common.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ReportService extends Remote {
    // Thống kê số lớp theo ngành
    Map<String, Integer> countClassesByMajor() throws RemoteException;

    // Thống kê số SV theo ngành
    Map<String, Integer> countStudentsByMajor() throws RemoteException;

    // Thống kê số SV theo trạng thái
    Map<String, Integer> countStudentsByStatus() throws RemoteException;
}
