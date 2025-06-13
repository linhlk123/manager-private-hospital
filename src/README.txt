
# 🏥 Manager Private Hospital

## 🔗 Link GitHub

👉 https://github.com/23520432/Manager-Private-Hospital.git

---

## 💻 Phần mềm sử dụng
- Java Swing 
- Oracle Database
- JDBC (Kết nối Oracle)
- NetBeans IDE 
- dbForge Studio 2022 for Oracle / SQL*Plus

---

## ⚙️ Hướng dẫn cài đặt

### 1. Clone project về máy

```bash
git clone https://github.com/23520432/Manager-Private-Hospital.git
cd Manager-Private-Hospital
```

---

### 2. Cài đặt Oracle Database

- Cài Oracle Database.
- Mở SQL*Plus, tạo user/schema:

```sql*Plus
/as sysdba
alter session set "_ORACLE_SCRIPT"=true;
CREATE USER QLBENHVIENTU identified by Admin123;
GRANT DBA to QLBENHVIENTU;
```

- Import CSDL bằng script database.

---

### 3. Cấu hình kết nối Oracle trong file `DBConnection.java`

```java
String connectionUrl = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;
String username = "QLBENHVIENTU";  // Tên người dùng Oracle
String password = "Admin123";  // Mật khẩu của người dùng Oracle;
```

> 📌 Thêm các thư viện vào Libraries trong project để chạy
jakarta.activation-2.0.1.jar  
jakarta.mail-2.0.1.jar  
json-20250107.jar  
ojdbc11-21.5.0.0.jar  
jfreechart-1.0.19.jar  
jcommon-1.0.16.jar  
core-3.5.3.jar  
itextpdf-5.5.13.3.jar  
javase-3.5.3.jar  
jcalendar-1.4.jar  
---

### 4. Build & chạy ứng dụng
-Đăng ký một số tài khoản
-Chạy các dữ liệu của các bảng trong database
