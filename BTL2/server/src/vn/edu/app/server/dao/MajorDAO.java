package vn.edu.app.server.dao;

import vn.edu.app.common.dto.MajorDTO;
import java.util.List;

public interface MajorDAO {
    List<MajorDTO> findAll();
    boolean exists(String majorId);
}
