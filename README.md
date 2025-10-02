<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>

<h2 align="center">
    HỆ THỐNG QUẢN LÝ SINH VIÊN BẰNG RMI
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

Hệ thống **Quản lý sinh viên bằng Java RMI** là một ứng dụng **Client–Server** phục vụ công tác quản lý sinh viên trong nhà trường.  
Dự án được phát triển với mục tiêu **đơn giản hóa công việc quản trị** đồng thời mang lại **giao diện trực quan và thân thiện** cho người dùng.

Hệ thống hỗ trợ hai nhóm đối tượng chính:

- 🎓 **Sinh viên (Student):**  
  - Đăng nhập vào hệ thống  
  - Xem và chỉnh sửa thông tin cá nhân  
  - Tra cứu bảng điểm và kết quả học tập  

- 🛠️ **Quản trị viên (Admin):**  
  - Quản lý sinh viên (thêm, sửa, xóa, tìm kiếm)  
  - Quản lý lớp, ngành, khoa, khóa học, học kỳ, năm học  
  - Quản lý học phần và điểm số của sinh viên  
  - Quản lý tài khoản và phân quyền người dùng  
  - Thống kê, báo cáo theo nhiều tiêu chí (lớp, ngành, khoa)  

---

## 🔧 2. Công nghệ & Thư viện sử dụng

- **Java RMI** – Xây dựng ứng dụng phân tán, cho phép gọi phương thức từ xa  
- **Java Swing** – Thiết kế giao diện người dùng (UI)  
- **FlatLaf** – Thư viện giao diện hiện đại giúp Swing đẹp mắt và dễ sử dụng hơn  
- **MySQL** – Hệ quản trị cơ sở dữ liệu quan hệ, lưu trữ thông tin sinh viên và điểm số  
- **JDBC** – Kết nối Java với MySQL  
- **Multithreading** – Cho phép server xử lý nhiều client đồng thời, tăng hiệu suất hệ thống  

---

## 🚀 3. Giao diện chi tiết

### 3.1. 🔑 Đăng nhập
<p align="center">
  <img src="https://github.com/user-attachments/assets/26a732a2-e621-44f0-9324-ff379b93f932" width="300"/>
  <img src="https://github.com/user-attachments/assets/efe451c1-790b-416a-a11f-dc1d840ec10e" width="300"/>
</p>
<p align="center"><em>Hình 1-2. Giao diện đăng nhập & thông báo lỗi</em></p>

---

### 3.2. 🛠️ Quản trị viên (Admin)

#### 3.2.1. 📊 Thống kê & Báo cáo
<p align="center">
  <img src="https://github.com/user-attachments/assets/5a9bfab8-8f75-4e7e-b3a3-c52574477155" width="800"/>
</p>
<p align="center"><em>Hình 3. Giao diện thống kê và báo cáo - có bộ lọc thống kê theo khoa, ngành, khóa</em></p>

---

#### 3.2.2. 🎓 Quản lý Sinh viên
<p align="center">
  <img src="https://github.com/user-attachments/assets/8a605f85-62d9-48fa-926a-6d3b2f1e1532" width="800"/>
</p>
<p align="center"><em>Hình 4. Giao diện quản lý sinh viên</em></p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/c7f85d63-1bb4-4671-a5f5-2c64e6bed7fc" width="800"/>
</p>
<p align="center"><em>Hình 5. Giao diện xem chi tiết thông tin sinh viên</em></p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/2f4017ed-3916-4b9d-8b36-363eb410e4ef" width="800"/>
</p>
<p align="center"><em>Hình 6. Giao diện nhập điểm cho sinh viên</em></p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/3d081e5a-d16c-4c25-bc66-02f861554480" width="300"/>

  <img src="https://github.com/user-attachments/assets/b9bf1ef1-15e1-4a73-a49a-0d1c8b7665a6" width="300"/>
</p>
<p align="center"><em>Hình 7-8. Giao diện thêm mới & sửa thông tin sinh viên</em></p>

---

#### 3.2.3. 🏫 Quản lý Lớp học
<p align="center">
  <img src="https://github.com/user-attachments/assets/0c2e485a-fa8a-4db8-b8d4-ec9066a6bd02" width="800"/>
</p>
<p align="center"><em>Hình 9. Giao diện quản lý lớp học</em></p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/d028bae6-d7f2-4473-bd36-021e3b6949a4" width="300"/>
  <img src="https://github.com/user-attachments/assets/b441b16d-a22d-4540-878b-cc7e86965404" width="300"/>
</p>
<p align="center"><em>Hình 10-11. Giao diện sửa & thêm lớp học</em></p>

### 3.3. 👨‍🎓 Sinh viên

#### 3.3.1. 📌 Trang cá nhân
<p align="center">
  <img src="https://github.com/user-attachments/assets/7d0e7fee-8888-427f-b9a6-6c0c77b34639" width="800"/>
</p>
<p align="center"><em>Hình 19. Giao diện trang cá nhân</em></p>

#### 3.3.2. 🖊️ Chỉnh sửa thông tin cá nhân
<p align="center">
  <img src="https://github.com/user-attachments/assets/0db197ce-1128-4da3-a2dd-27795bd31992" width="800"/>
</p>
<p align="center"><em>Hình 20. Giao diện chỉnh sửa thông tin cá nhân</em></p>

#### 3.3.3. 📑 Xem bảng điểm
<p align="center">
  <img src="https://github.com/user-attachments/assets/12517805-d4e2-4726-a7fe-63f6db0392b6" width="800"/>
</p>
<p align="center"><em>Hình 21. Giao diện bảng điểm sinh viên</em></p>

---

## ⚙️ 5. Cài đặt & Cấu hình

### 🔹 Bước 1: Tải dự án về
- Clone repository từ GitHub:  
```bash
git clone https://github.com/huuhuy2910/Network-Programming.git
cd Network-Programming/BT2/qlsv_rmi
````

* Hoặc tải file `.zip` dự án về máy, sau đó giải nén và mở trong **Eclipse/IntelliJ IDEA**.

---

### 🔹 Bước 2: Thêm thư viện vào Build Path

1. Trong Eclipse, click chuột phải vào project `qlsv_rmi` → **Properties**.
2. Chọn **Java Build Path** → tab **Libraries**.
3. Nhấn **Add JARs…**, chọn 2 file sau trong thư mục `qlsv_rmi/lib`:

   * `flatlaf-xxx.jar` (thư viện giao diện FlatLaf)
   * `mysql-connector-j-8.0.xx.jar` (thư viện kết nối MySQL)
4. Nhấn **Apply and Close** để lưu cấu hình.

---

### 🔹 Bước 3: Khởi tạo cơ sở dữ liệu

Import file `sql/student_mgmt.sql` hoặc chạy script sau trong MySQL:

```sql
DROP DATABASE IF EXISTS student_mgmt;
CREATE DATABASE student_mgmt CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_mgmt;

-- Bảng Sinh viên
CREATE TABLE students (
  student_id VARCHAR(20) PRIMARY KEY,
  full_name VARCHAR(150) NOT NULL,
  gender ENUM('M','F') NOT NULL,
  class_name VARCHAR(50) NOT NULL,
  course VARCHAR(50) NOT NULL,
  major VARCHAR(100) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(150),
  address VARCHAR(255),
  hometown VARCHAR(150),
  status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE'
);

-- Bảng Người dùng
CREATE TABLE users (
  username VARCHAR(20) PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  role ENUM('STUDENT','ADMIN') DEFAULT 'STUDENT',
  FOREIGN KEY (username) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Bảng Học phần
CREATE TABLE courses (
  course_id VARCHAR(20) PRIMARY KEY,
  course_name VARCHAR(200) NOT NULL,
  credits INT DEFAULT 3,
  semester VARCHAR(20)
);

-- Bảng Điểm
CREATE TABLE grades (
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id VARCHAR(20) NOT NULL,
  course_id VARCHAR(20) NOT NULL,
  score FLOAT CHECK (score >= 0 AND score <= 10),
  grade_note VARCHAR(255),
  UNIQUE KEY uq_student_course (student_id, course_id),
  FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
  FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Tài khoản mặc định
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN');
```

---

### 🔹 Bước 4: Cấu hình kết nối DB trong code

Mở file `DBConnection.java` và chỉnh lại theo thông tin MySQL của bạn:

```java
private static final String URL = "jdbc:mysql://localhost:3306/student_mgmt";
private static final String USER = "root";     // user MySQL
private static final String PASS = "123456";   // password MySQL
```

---

### 🔹 Bước 5: Chạy Server

1. Trong Eclipse, vào package `server`.
2. Mở file **ServerApp.java**.
3. Nhấn **Run ▶** để khởi động server.
   👉 Console hiển thị: `Server is running on port 1099...`

---

### 🔹 Bước 6: Chạy Client

1. Trong Eclipse, vào package `client`.
2. Mở file **ClientApp.java**.
3. Nhấn **Run ▶** để khởi động client.
   👉 Giao diện đăng nhập sẽ xuất hiện.

Mặc định client kết nối `localhost`.
Nếu server chạy trên máy khác (ví dụ `192.168.1.10`), chỉnh IP trong `ClientApp` trước khi chạy.

```

---

📌 FLow như sau:  
1. Tải dự án  
2. Thêm lib vào Build Path (`flatlaf` + `mysql-connector`)  
3. Khởi tạo DB  
4. Cấu hình DBConnection  
5. Run ServerApp  
6. Run ClientApp  

```


## 👨‍💻 6. Thông tin liên hệ

* **Sinh viên thực hiện:** Nguyễn Hữu Huy  
* **Lớp:** CNTT 16-01  
* **Email:** [nguyenhuuhuy489@gmail.com](mailto:nguyenhuuhuy489@gmail.com)

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.
```

Bạn có muốn mình thêm **sơ đồ ERD database** (quan hệ Students – Users – Courses – Grades) ngay trong README để người đọc dễ hình dung không?
```
