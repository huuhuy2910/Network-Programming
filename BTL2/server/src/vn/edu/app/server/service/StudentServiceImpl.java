package vn.edu.app.server.service;

import vn.edu.app.common.dto.StudentDTO;
import vn.edu.app.common.remote.StudentService;
import vn.edu.app.server.dao.StudentDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

public class StudentServiceImpl extends UnicastRemoteObject implements StudentService {
    private final StudentDAO studentDAO;

    public StudentServiceImpl(StudentDAO dao) throws RemoteException {
        super();
        this.studentDAO = dao;
    }

    @Override
    public StudentDTO getById(String studentId) throws RemoteException {
        Optional<StudentDTO> op = studentDAO.findById(studentId);
        return op.orElse(null);
    }

    @Override
    public boolean updateStudent(StudentDTO s) throws RemoteException {
        // có thể thêm validation tại đây (email/phone form…)
        return studentDAO.update(s);
    }
}
