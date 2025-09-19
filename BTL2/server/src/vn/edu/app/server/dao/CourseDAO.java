package vn.edu.app.server.dao;

import vn.edu.app.common.dto.CourseDTO;
import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    boolean insert(CourseDTO c);
    boolean update(CourseDTO c);
    boolean delete(String courseId);
    List<CourseDTO> findAll();
    Optional<CourseDTO> findById(String courseId);
}
