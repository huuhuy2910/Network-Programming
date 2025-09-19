package vn.edu.app.common.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import vn.edu.app.common.dto.StudentDTO;

public interface StudentService extends Remote {
    StudentDTO getById(String studentId) throws RemoteException;
    boolean updateStudent(StudentDTO student) throws RemoteException;
}
