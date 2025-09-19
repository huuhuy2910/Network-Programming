package vn.edu.app.server.dao;

import vn.edu.app.common.dto.ClassDTO;
import java.util.List;

public interface ClassDAO {
    List<ClassDTO> findAll();
    boolean exists(String classId);
}
