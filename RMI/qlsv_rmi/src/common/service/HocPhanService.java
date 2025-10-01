package common.service;

import common.dto.HocPhan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HocPhanService extends Remote {
    List<HocPhan> getAll() throws RemoteException;

    HocPhan getById(String id) throws RemoteException;

    boolean insert(HocPhan hocPhan) throws RemoteException;

    boolean update(HocPhan hocPhan) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<HocPhan> search(String keyword) throws RemoteException;
}
