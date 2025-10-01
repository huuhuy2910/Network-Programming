package common.service;

import common.dto.Khoa;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface KhoaService extends Remote {
    List<Khoa> getAll() throws RemoteException;

    Khoa getById(String id) throws RemoteException;

    boolean insert(Khoa khoa) throws RemoteException;

    boolean update(Khoa khoa) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<Khoa> search(String keyword) throws RemoteException;
}
