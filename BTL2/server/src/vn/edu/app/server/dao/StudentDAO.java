package vn.edu.app.server.dao;

import vn.edu.app.common.dto.StudentDTO;
import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    Optional<StudentDTO> findById(String studentId);
    boolean insert(StudentDTO s);
    boolean update(StudentDTO s);
    boolean delete(String studentId);
    List<StudentDTO> findAll();
}
