package common.service;

import common.dto.Diem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DiemService extends Remote {
    List<Diem> getAll() throws RemoteException;

    Diem getById(long id) throws RemoteException;

    boolean insert(Diem diem) throws RemoteException;

    boolean update(Diem diem) throws RemoteException;

    boolean delete(long id) throws RemoteException;

    List<Diem> search(String keyword) throws RemoteException;

    List<Diem> getBySinhVien(String svId) throws RemoteException;

    double calculateGpa(String svId) throws RemoteException;
}
