<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
    XÂY DỰNG PHẦN MỀM QUẢN LÝ SINH VIÊN THÔNG QUA GIAO THỨC RMI
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="FIT DNU Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---

## 📖 1. Giới thiệu
Đề tài minh họa việc xây dựng **hệ thống quản lý sinh viên** theo mô hình **Client–Server** sử dụng:
- **Java RMI (Remote Method Invocation)** để gọi phương thức từ xa.
- **Java Swing** để xây dựng giao diện trực quan cho người dùng.
- **File I/O + Serialization** để lưu trữ dữ liệu sinh viên thay vì dùng cơ sở dữ liệu.

Chức năng chính:
- Thêm, sửa, xóa, tìm kiếm và hiển thị danh sách sinh viên.
- Quản lý thông tin: mã sinh viên, họ tên, ngày sinh, lớp, khoa, số điện thoại, email, quê quán.
- Hỗ trợ nhiều client cùng kết nối đến server.

---

## 🔧 2. Công nghệ sử dụng
- [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)  
- **Java RMI** – gọi phương thức từ xa, xây dựng ứng dụng phân tán.  
- **Java Swing** – thiết kế giao diện người dùng trực quan.  
- **File I/O + Serialization** – lưu trữ dữ liệu sinh viên vào file.  
- **Multithreading** – xử lý đồng thời nhiều kết nối client, đồng bộ dữ liệu bằng `synchronized`.  
- IDE khuyến nghị: Eclipse / IntelliJ IDEA

---

## 🚀 3. Hình ảnh giao diện
<p align="center">
  <img src="docs/screenshot_main.png" alt="Main UI" width="700"/>
  <br/>
  <em>Giao diện chính quản lý sinh viên</em>
</p>

---

## 📝 4. Hướng dẫn cài đặt và sử dụng

### Bước 1: Clone project
```bash
git clone https://github.com/your-repo/StudentManagementRMI.git
cd StudentManagementRMI
```

### Bước 2: Chạy Server
```bash
cd server
javac *.java
java server.ServerMain
```

Server khởi động trên cổng `1099` với dịch vụ `StudentService`.

### Bước 3: Chạy Client
```bash
cd client
javac *.java
java client.ClientMain
```

- Client mặc định kết nối `localhost`.  
- Nếu server chạy trên máy khác:  
  ```bash
  java client.ClientMain 192.168.x.x
  ```

---
### Thông tin liên hệ:
Họ tên: Nguyễn Hữu Huy.

Lớp: CNTT 16-01.

Email: nguyenhuuhuy489@gmail.com.

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.
