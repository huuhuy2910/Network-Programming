package common.service;

import common.dto.HocKy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HocKyService extends Remote {
    List<HocKy> getAll() throws RemoteException;

    HocKy getById(String id) throws RemoteException;

    boolean insert(HocKy hocKy) throws RemoteException;

    boolean update(HocKy hocKy) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<HocKy> search(String keyword) throws RemoteException;
}
