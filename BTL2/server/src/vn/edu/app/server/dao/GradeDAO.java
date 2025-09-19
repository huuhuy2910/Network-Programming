package vn.edu.app.server.dao;

import vn.edu.app.common.dto.GradeDTO;
import java.util.List;

public interface GradeDAO {
    boolean upsert(GradeDTO g); // insert or update
    List<GradeDTO> findByStudent(String studentId);
}
