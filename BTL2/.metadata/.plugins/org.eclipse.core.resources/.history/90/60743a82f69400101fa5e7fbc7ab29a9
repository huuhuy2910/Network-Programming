package vn.edu.app.server.service;

import vn.edu.app.common.dto.*;
import vn.edu.app.common.remote.AdminService;
import vn.edu.app.server.dao.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

public class AdminServiceImpl extends UnicastRemoteObject implements AdminService {
    private final StudentDAO studentDAO;
    private final ClassDAO classDAO;
    private final MajorDAO majorDAO;
    private final CourseDAO courseDAO;
    private final GradeDAO gradeDAO;

    public AdminServiceImpl(StudentDAO studentDAO, ClassDAO classDAO, MajorDAO majorDAO,
                            CourseDAO courseDAO, GradeDAO gradeDAO) throws RemoteException {
        super();
        this.studentDAO = Objects.requireNonNull(studentDAO);
        this.classDAO = Objects.requireNonNull(classDAO);
        this.majorDAO = Objects.requireNonNull(majorDAO);
        this.courseDAO = Objects.requireNonNull(courseDAO);
        this.gradeDAO = Objects.requireNonNull(gradeDAO);
    }

    // Sinh viên
    @Override public boolean addStudent(StudentDTO s) { return studentDAO.insert(s); }
    @Override public boolean updateStudent(StudentDTO s) { return studentDAO.update(s); }
    @Override public boolean deleteStudent(String studentId) { return studentDAO.delete(studentId); }
    @Override public List<StudentDTO> getAllStudents() { return studentDAO.findAll(); }

    // Lớp & Ngành
    @Override public List<ClassDTO> getAllClasses() { return classDAO.findAll(); }
    @Override public List<MajorDTO> getAllMajors() { return majorDAO.findAll(); }

    // Học phần
    @Override public boolean addCourse(CourseDTO c) { return courseDAO.insert(c); }
    @Override public boolean updateCourse(CourseDTO c) { return courseDAO.update(c); }
    @Override public boolean deleteCourse(String courseId) { return courseDAO.delete(courseId); }
    @Override public List<CourseDTO> getAllCourses() { return courseDAO.findAll(); }

    // Điểm
    @Override public boolean assignGrade(GradeDTO g) { return gradeDAO.upsert(g); }
    @Override public List<GradeDTO> getGradesByStudent(String studentId) { return gradeDAO.findByStudent(studentId); }
}
