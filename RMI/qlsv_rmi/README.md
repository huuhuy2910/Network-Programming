# Hệ thống Quản lý sinh viên RMI

Dự án mẫu mô phỏng hệ thống quản lý sinh viên sử dụng **Java RMI** kết hợp **Swing UI** và cơ sở dữ liệu **MySQL**.

## Cấu trúc dự án

```
qlsv_rmi/
 ├── src/
 │   ├── common/        # DTO & interface RMI
 │   ├── server/        # Server RMI, DAO, service implementation
 │   └── client/        # Ứng dụng khách Swing
 └── bin/               # Thư mục biên dịch (nếu dùng Eclipse)
```

## Chuẩn bị

1. Cài đặt MySQL, tạo cơ sở dữ liệu `qlsv_rmi` và import dữ liệu mẫu.
2. Cập nhật thông tin kết nối trong `server.util.DBConnection` nếu cần.
3. Thêm thư viện JDBC MySQL (`mysql-connector-j`) và FlatLaf (`com.formdev:flatlaf`) vào classpath.

## Chạy ứng dụng

1. Biên dịch toàn bộ mã nguồn (ví dụ bằng `javac` hoặc trong IDE).
2. Chạy server RMI:
   ```bash
   java server.ServerApp
   ```
3. Chạy ứng dụng khách:
   ```bash
   java client.ClientApp
   ```
4. Đăng nhập bằng tài khoản admin hoặc sinh viên đã có trong CSDL.

## Tính năng chính

- Quản lý khoa, ngành, lớp, sinh viên, học phần, điểm, tài khoản.
- Tính điểm tổng kết tự động theo công thức `$0.4 * điểm\_quá\_trình + 0.6 * điểm\_thi$`.
- Phân quyền rõ ràng giữa **ADMIN** và **SINHVIEN**.
- Giao diện Swing phẳng hiện đại với FlatLaf.

## Ghi chú

- Đây là mã mẫu tham khảo; cần bổ sung xử lý ngoại lệ, logging và bảo mật (hash mật khẩu, phân trang...) cho môi trường thực tế.
- Đảm bảo chạy `rmiregistry` nếu cần khi triển khai ngoài IDE.
