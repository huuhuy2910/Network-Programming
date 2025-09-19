package vn.edu.app.common.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import vn.edu.app.common.dto.*;

public interface AdminService extends Remote {
    // Quản lý sinh viên
    boolean addStudent(StudentDTO s) throws RemoteException;
    boolean updateStudent(StudentDTO s) throws RemoteException;
    boolean deleteStudent(String studentId) throws RemoteException;
    List<StudentDTO> getAllStudents() throws RemoteException;

    // Quản lý lớp & ngành
    List<ClassDTO> getAllClasses() throws RemoteException;
    List<MajorDTO> getAllMajors() throws RemoteException;

    // Quản lý học phần
    boolean addCourse(CourseDTO c) throws RemoteException;
    boolean updateCourse(CourseDTO c) throws RemoteException;
    boolean deleteCourse(String courseId) throws RemoteException;
    List<CourseDTO> getAllCourses() throws RemoteException;

    // Quản lý điểm
    boolean assignGrade(GradeDTO g) throws RemoteException;
    List<GradeDTO> getGradesByStudent(String studentId) throws RemoteException;
}
