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
Đề tài minh họa việc xây dựng **hệ thống quản lý sinh viên** theo mô hình **Client–Server** sử dụng:
- **Java RMI (Remote Method Invocation)** để gọi phương thức từ xa giữa client và server.
- **Java Swing** để xây dựng giao diện trực quan cho người dùng.
- **MySQL Database** để lưu trữ và quản lý dữ liệu sinh viên, học phần, điểm số.
- **Phân quyền tài khoản**: Quản trị viên (ADMIN) và Sinh viên (STUDENT).

🔑 **Chức năng chính:**
- **Sinh viên:**
  - Đăng nhập, xem và chỉnh sửa thông tin cá nhân.
  - Xem bảng điểm các học phần.
- **Quản trị viên:**
  - Quản lý sinh viên (thêm, sửa, xóa, tìm kiếm).
  - Quản lý lớp, ngành, học phần.
  - Quản lý điểm học phần.
  - Báo cáo thống kê số lượng sinh viên theo lớp/ngành.

---

## 🔧 2. Công nghệ sử dụng
- [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)  
- **Java RMI** – gọi phương thức từ xa, xây dựng ứng dụng phân tán.  
- **Java Swing** – thiết kế giao diện trực quan.  
- **MySQL** – hệ quản trị cơ sở dữ liệu quan hệ.  
- **JDBC** – kết nối Java với MySQL.  
- **Multithreading** – server xử lý đồng thời nhiều client.  
- IDE khuyến nghị: Eclipse / IntelliJ IDEA.  

---

## 🚀 3. Hình ảnh giao diện

<p align="center">

### 🔑 3.1. Giao diện đăng nhập  
<img width="400" alt="Login1" src="https://github.com/user-attachments/assets/918240e8-d60f-4ec0-9289-7ae574eeea2c" />
<img width="400" alt="Login2" src="https://github.com/user-attachments/assets/89ec43d1-a91d-4c94-86a2-7e0837d5e034" />

---

### 🛠️ 3.2. Giao diện Admin  
**Hình 1: Quản lý sinh viên**  
<img width="800" alt="Main Admin" src="https://github.com/user-attachments/assets/f9f594bb-3ddb-4801-adab-14769efa4e44" />

**Hình 2: Thêm sinh viên**  
<img width="400" alt="Add Student" src="https://github.com/user-attachments/assets/df1dcefb-a7f3-4445-8cc3-d6b2d8e0abbf" />

**Hình 3: Sửa sinh viên**  
<img width="400" alt="Edit Student" src="https://github.com/user-attachments/assets/fbff03bc-6d1a-4233-931b-29ce26a21935" />

**Hình 4: Danh sách lớp học**  
<img width="800" alt="Class List" src="https://github.com/user-attachments/assets/1b6035ad-01b2-4c44-8d75-6f600799a374" />

**Hình 5: Quản lý học phần**  
<img width="800" alt="Course Management" src="https://github.com/user-attachments/assets/98e67ff1-2d8b-4698-9df4-e78ae2e3137a" />

**Hình 6: Quản lý điểm**  
<img width="800" alt="Grade Management" src="https://github.com/user-attachments/assets/d0dbfa1d-09e3-422f-a034-2a11d5db392c" />

**Hình 7: Thống kê / Báo cáo**  
<img width="800" alt="Reports" src="https://github.com/user-attachments/assets/0d622af6-10f3-4a57-a04d-b1d895d50159" />

---

### 🎓 3.3. Giao diện Sinh viên  
**Hình 8: Thay đổi thông tin cá nhân**  
<img width="800" alt="Student Profile" src="https://github.com/user-attachments/assets/a6ccf07d-a9fd-4ff1-8170-b7b01fdba91c" />

**Hình 9: Xem bảng điểm**  
<img width="800" alt="Student Grades" src="https://github.com/user-attachments/assets/b7c62da0-fa86-4f02-821a-595ce9d6302a" />

</p>

---

## 📝 4. Cài đặt & Cấu hình

### 🔹 Bước 1: Clone project
```bash
git clone gh repo clone huuhuy2910/Network-Programming/BT2.git
cd StudentManagementRMI
````

### 🔹 Bước 2: Xây dựng cơ sở dữ liệu

Import file `student_mgmt.sql` hoặc chạy script sau trong MySQL:

```sql
DROP DATABASE IF EXISTS student_mgmt;
CREATE DATABASE student_mgmt CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_mgmt;

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

CREATE TABLE users (
  username VARCHAR(20) PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  role ENUM('STUDENT','ADMIN') DEFAULT 'STUDENT',
  FOREIGN KEY (username) REFERENCES students(student_id) ON DELETE CASCADE
);

CREATE TABLE courses (
  course_id VARCHAR(20) PRIMARY KEY,
  course_name VARCHAR(200) NOT NULL,
  credits INT DEFAULT 3,
  semester VARCHAR(20)
);

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

INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN');
```

Cập nhật file `DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/student_mgmt";
private static final String USER = "root";
private static final String PASS = "yourpassword";
```

### 🔹 Bước 3: Chạy Server

```bash
cd server
javac -cp ../shared:../lib/mysql-connector-j-8.0.xx.jar src/main/java/vn/edu/app/server/*.java
java -cp ../shared:../lib/mysql-connector-j-8.0.xx.jar:. vn.edu.app.server.ServerApp
```

### 🔹 Bước 4: Chạy Client

```bash
cd client
javac -cp ../shared src/main/java/vn/edu/app/client/*.java
java -cp ../shared:. vn.edu.app.client.ui.LoginFrame
```

👉 Mặc định kết nối `localhost`.
Nếu server chạy trên máy khác:

```bash
java -cp ../shared:. vn.edu.app.client.ui.LoginFrame 192.168.x.x
```

---

## 👨‍💻 5. Thông tin liên hệ

* **Sinh viên thực hiện:** Nguyễn Hữu Huy
* **Lớp:** CNTT 16-01
* **Email:** [nguyenhuuhuy489@gmail.com](mailto:nguyenhuuhuy489@gmail.com)

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

```

Bạn có muốn mình thêm **sơ đồ ERD database** (quan hệ Students – Users – Courses – Grades) ngay trong README để người đọc dễ hình dung không?
```
