# OOP_N03_2025_K17
## ỨNG DỤNG QUẢN LÝ SÁCH

---

### GIỚI THIỆU

Ứng dụng web quản lý thư viện giúp người dùng quản lý danh sách sách, thông tin người mượn, và các phiếu mượn một cách thuận tiện và trực quan.

Ứng dụng cung cấp giao diện thân thiện, hỗ trợ các thao tác thêm, sửa, xóa và lọc dữ liệu, cùng với khả năng theo dõi trạng thái mượn sách.

#### Tính năng chính:

- **Quản lý người mượn**: Thêm, sửa, xóa thông tin người dùng.
- **Quản lý sách**: Quản lý thông tin sách và trạng thái (còn, đã mượn).
- **Quản lý phiếu mượn**: Tạo, cập nhật và theo dõi các phiếu mượn sách.

---

### THÀNH VIÊN NHÓM

- **Nguyễn Thị Quỳnh Anh - 23010147**: Phát triển phần mềm  

---

### CHỨC NĂNG CHÍNH

#### 1. Quản lý người dùng

- Thêm, sửa, xóa thông tin người dùng (tên, email, số điện thoại,...).
- Hiển thị chi tiết thông tin người mượn, bao gồm lịch sử mượn sách.
- Lọc và tìm kiếm người mượn theo tên hoặc email.

#### 2. Quản lý sách

- Thêm, sửa, xóa thông tin sách (mã sách, tiêu đề, tác giả, trạng thái).
- Lọc và hiển thị danh sách sách theo trạng thái (available, borrowed).
- Hiển thị thông tin chi tiết sách.

#### 3. Quản lý phiếu mượn

- Tạo phiếu mượn mới cho người mượn và sách.
- Cập nhật thông tin phiếu mượn (ngày mượn, ngày đến hạn, trạng thái).
- Xóa phiếu mượn (chỉ khi sách chưa được trả).
- Tìm kiếm và lọc phiếu mượn theo ngày mượn, ngày đến hạn, hoặc trạng thái (BORROWED, RETURNED, OVERDUE).
- Hiển thị chi tiết phiếu mượn, bao gồm thông tin người mượn và sách.

---

### LINK DEMO

- **Video Demo**: https://youtu.be/tK-wlnSBl5M

---

### THIẾT KẾ CƠ SỞ DỮ LIỆU

#### Bảng: Books

Trường              | Kiểu dữ liệu   | Mô tả
--------------------|----------------|--------------------------------------------
id                  | Integer        | Mã sách (khóa chính, tự tăng)
title               | String         | Tiêu đề sách (bắt buộc, không để trống)
author              | String         | Tác giả sách (bắt buộc, không để trống)
category            | String         | Thể loại sách (bắt buộc, không để trống)
available           | Boolean        | Trạng thái có sẵn (true/false, mặc định true)
status              | String         | Trạng thái mô tả thêm (có thể để trống)


#### Bảng: Users

Trường              | Kiểu dữ liệu   | Mô tả
--------------------|----------------|--------------------------------------------
id                  | Integer        | Mã người dùng (khóa chính, tự tăng)
name                | String         | Họ tên người dùng (bắt buộc, không để trống)
email               | String         | Địa chỉ email (bắt buộc, định dạng hợp lệ, duy nhất)
role                | String         | Vai trò (bắt buộc: 'student' hoặc 'admin')
phone               | String         | Số điện thoại (không bắt buộc)
studentId           | String         | Mã sinh viên (nếu là student)
className           | String         | Tên lớp (nếu là student)


#### Bảng: BorrowRecord

Trường              | Kiểu dữ liệu   | Mô tả
--------------------|----------------|--------------------------------------------
id                  | Integer        | Mã phiếu mượn (khóa chính, tự tăng)
user_id             | User           | Người mượn (khóa ngoại - quan hệ nhiều-một)
book_id             | Book           | Sách mượn (khóa ngoại - quan hệ nhiều-một)
borrow_date         | LocalDate      | Ngày mượn sách (bắt buộc)
due_date            | LocalDate      | Ngày đến hạn trả (bắt buộc)
return_date         | LocalDate      | Ngày thực tế trả (có thể để trống)
status              | Enum           | Trạng thái phiếu mượn: BORROWED, RETURNED, OVERDUE
extend_count        | Integer        | Số lần gia hạn (mặc định là 0, không để trống)

---

### 📚 CÔNG NGHỆ SỬ DỤNG

Dự án được phát triển theo kiến trúc **Spring Boot MVC** với giao diện người dùng động được tạo từ Thymeleaf. Dưới đây là tổng quan các công nghệ và công cụ được sử dụng trong quá trình phát triển:

---

## 1. 🖼️ Frontend

- **Template Engine**:  
  - Sử dụng **Thymeleaf** để xây dựng giao diện HTML động.  
  - Tích hợp trực tiếp trong Spring Boot, hỗ trợ binding dữ liệu giữa backend và frontend một cách dễ dàng.

---

## 2. 🔧 Backend

- **Ngôn ngữ lập trình**: Java 17  
- **Framework chính**: Spring Boot `v3.5.3`  
- **Kiến trúc ứng dụng**: Mô hình MVC bao gồm:
  - `Controller`: Xử lý các HTTP request và trả về response.
  - `Service`: Xử lý logic nghiệp vụ.
  - `Repository`: Giao tiếp với cơ sở dữ liệu, sử dụng Spring Data JPA.
  - `Model`: Đại diện cho các thực thể dữ liệu trong hệ thống.

- **Thư viện tích hợp**:
  - `Spring Web`: Phát triển RESTful API và xử lý request/response.
  - `Spring Data JPA`: Truy cập và quản lý cơ sở dữ liệu.

---

## 3. 💾 Cơ Sở Dữ Liệu

- **Hệ quản trị CSDL**: MySQL (được cung cấp bởi [Aiven Cloud](https://aiven.io/))
- **Kết nối bảo mật**: Sử dụng cấu hình **SSL** để đảm bảo kết nối an toàn giữa ứng dụng và cơ sở dữ liệu.
- **Tích hợp**: Kết nối qua Spring Data JPA, hỗ trợ truy vấn tự động và hiệu quả.

---

## 4. ⚙️ Công Cụ Build

- **Build Tool**: Maven  
  - Quản lý dependency.
  - Biên dịch và đóng gói ứng dụng.

---
---

### 📁 CẤU TRÚC THƯ MỤC
```

Webnc/
├── .vscode/
├── springbootApp/
│ ├── complete/
│ │ ├── pom.xml
│ │ ├── mvnw
│ │ ├── mvnw.cmd
│ │ ├── src/
│ │ │ ├── main/java/com/example/servingwebcontent/
│ │ │ │ ├── controller/
│ │ │ │ │ ├── BorrowRecordController.java
│ │ │ │ │ ├── UserController.java
│ │ │ │ │ ├── BookController.java
│ │ │ │ ├── model/
│ │ │ │ │ ├── User.java
│ │ │ │ │ ├── Book.java
│ │ │ │ │ ├── BorrowRecord.java
│ │ │ │ ├── repository/
│ │ │ │ │ ├── UserRepository.java
│ │ │ │ │ ├── BookRepository.java
│ │ │ │ │ ├── BorrowRecordRepository.java
│ │ │ │ ├── service/
│ │ │ │ │ ├── UserService.java
│ │ │ │ │ ├── BookService.java
│ │ │ │ │ ├── BorrowRecordService.java
│ │ │ │ ├── ServingWebContentApplication.java
│ │ │ ├── resources/
│ │ │ │ ├── templates/
│ │ │ │ │ ├── borrows/
│ │ │ │ │ │ ├── add.html
│ │ │ │ │ │ ├── list.html
│ │ │ │ │ │ ├── my-borrows.html
│ │ │ │ │ │ ├── edit.html
│ │ │ │ │ ├── users/
│ │ │ │ │ │ ├── add.html
│ │ │ │ │ │ ├── list.html
│ │ │ │ │ │ ├── edit.html
│ │ │ │ │ ├── books/
│ │ │ │ │ │ ├── add.html
│ │ │ │ │ │ ├── list.html
│ │ │ │ │ │ ├── edit.html
│ │ │ │ │ ├── index.html
│ │ │ ├── application.properties
│ │ ├── test/java/com/example/servingwebcontent/
│ │ │ ├── testcontroller/
│ │ │ │ ├── BorrowRecordControllerTest.java
│ │ │ │ ├── UserControllerTest.java
│ │ │ │ ├── BookControllerTest.java
│ │ │ ├── ServingWebContentApplicationTest.java
├── .gitignore
├── README.md

```
# 🧠 MÔ HÌNH VÀ CHỨC NĂNG HỆ THỐNG

---

## 1. ⚙️ MÔ HÌNH HỆ THỐNG - KIẾN TRÚC MVC (Model - View - Controller)

Ứng dụng được phát triển theo kiến trúc **MVC**, giúp tách biệt rõ ràng giữa:

- **Model** (Dữ liệu)
- **View** (Giao diện)
- **Controller** (Xử lý logic và điều hướng)

### 🧩 Model

Các lớp `Model` đại diện cho dữ liệu trong hệ thống, ánh xạ trực tiếp tới các bảng trong MySQL thông qua JPA.

#### ✅ User.java
- **Mục đích**: Đại diện cho người mượn sách.
- **Thuộc tính**:
  - `id`: Mã người dùng (PK)
  - `name`: Tên (bắt buộc)
  - `email`: Địa chỉ email
  - `phoneNumber`: Số điện thoại (9–11 chữ số)
  - `address`: Địa chỉ

#### ✅ Book.java
- **Mục đích**: Quản lý thông tin sách trong thư viện.
- **Thuộc tính**:
  - `id`: Mã sách (PK)
  - `title`: Tiêu đề (bắt buộc)
  - `author`: Tác giả (bắt buộc)
  - `status`: Trạng thái (available / borrowed)

#### ✅ BorrowRecord.java
- **Mục đích**: Quản lý phiếu mượn sách.
- **Thuộc tính**:
  - `id`: Mã phiếu mượn (PK, tự tăng)
  - `user`: Người mượn (quan hệ nhiều-một)
  - `book`: Sách được mượn (nhiều-một)
  - `borrowDate`: Ngày mượn (bắt buộc)
  - `dueDate`: Ngày đến hạn (bắt buộc)
  - `returnDate`: Ngày trả (có thể null)
  - `status`: BORROWED / RETURNED / OVERDUE
  - `extendCount`: Số lần gia hạn (mặc định 0)

---

### 🖼️ View (Giao Diện Người Dùng)

Sử dụng **Thymeleaf** để xây dựng giao diện HTML động.

#### 📄 Giao diện quản lý phiếu mượn (borrows/)
- `list.html`: Danh sách các phiếu mượn.
- `add.html`: Tạo mới phiếu mượn.
- `edit.html`: Cập nhật phiếu mượn.
- `my-borrows.html`: Phiếu mượn của người dùng hiện tại.

#### 📄 Giao diện quản lý sách (book/)
- `list.html`: Danh sách sách trong thư viện.
- `add.html`: Thêm sách mới.

#### 📄 Giao diện quản lý người mượn (user/)
- `list.html`: Danh sách người mượn.
- `add.html`: Thêm người mượn mới.

#### ⚠️ Thông báo
- Hiển thị lỗi validation (VD: "Người mượn không được để trống").
- Hiển thị thành công (VD: "Thêm phiếu mượn thành công").

---

### 🕹️ Controller

#### 🔄 BorrowRecordController
- `GET /borrows`: Danh sách phiếu mượn
- `GET /borrows/add`: Hiển thị form thêm phiếu mượn
- `POST /borrows/add`: Xử lý thêm phiếu mượn
- `GET /borrows/edit/{id}`: Sửa phiếu mượn
- `GET /borrows/my-borrows`: Phiếu mượn của người dùng hiện tại

#### 🔄 UserController
- `GET /users`: Danh sách người mượn
- `GET /users/add`: Thêm người mượn
- `POST /users/add`: Xử lý thêm

#### 🔄 BookController
- `GET /books`: Danh sách sách
- `GET /books/add`: Thêm sách
- `POST /books/add`: Xử lý thêm sách

---

## 2. 🛠️ CHỨC NĂNG HỆ THỐNG

---

### 👤 Quản Lý Người Mượn

- ✅ **Thêm người mượn**
- ✏️ **Chỉnh sửa thông tin người mượn**
- ❌ **Xóa người mượn** (chỉ khi không có phiếu mượn)
- 🔍 **Tìm kiếm** theo tên hoặc email
- 🔎 **Lọc** theo ngày đăng ký hoặc trạng thái
- 📄 **Xem chi tiết** người mượn và lịch sử mượn sách

---

### 📚 Quản Lý Sách

- ✅ **Thêm sách**
- ✏️ **Chỉnh sửa sách**
- ❌ **Xóa sách** (nếu chưa có phiếu mượn)
- 🔍 **Tìm kiếm** theo tiêu đề, tác giả
- 🔎 **Lọc** theo trạng thái (available / borrowed)
- 📄 **Xem chi tiết** sách

---

### 📄 Quản Lý Phiếu Mượn

- ✅ **Tạo phiếu mượn**
  - Chọn người mượn, sách, nhập ngày mượn và ngày đến hạn
  - Trạng thái mặc định: `BORROWED`
- ✏️ **Cập nhật phiếu mượn**
  - Ngày trả
  - Trạng thái (`BORROWED`, `RETURNED`, `OVERDUE`)
  - Gia hạn mượn (tăng `extendCount`)
- ❌ **Xóa phiếu mượn** (chỉ khi trạng thái là `RETURNED`)
- 🔍 **Tìm kiếm** theo mã, tên người mượn, tên sách
- 🔎 **Lọc** theo ngày, trạng thái
- 📄 **Xem chi tiết** phiếu mượn

---
## UML DIAGRAM

### CLASS DIAGRAM
![classdiagram](https://github.com/user-attachments/assets/708cccda-9699-4b73-aa48-ba977cad71d6)

### SEQUENCE DIAGRAM
![sequencebook](https://github.com/user-attachments/assets/c44a1ba5-4410-49c4-9332-02664c4ec8f6)
![sequenceuser](https://github.com/user-attachments/assets/069219d4-7da4-4e93-8db2-fc39690866b3)
![sequenceborrow](https://github.com/user-attachments/assets/c7c7e7c8-a8cc-4f16-8397-c1970efff07b)

### ACTIVITY DIAGRAM
![activity](https://github.com/user-attachments/assets/6d89d0c1-7371-4bf8-b718-ea2af1ab957f)

### Giao diện

## Menu
![menu](https://github.com/user-attachments/assets/a1f0a448-447c-4da8-bd15-ededc2bfe798)

## Books
![image](https://github.com/user-attachments/assets/096f4891-4086-4df1-b7f2-b0ea2bd57ba8)

## Users
![image](https://github.com/user-attachments/assets/0576777d-c875-4f74-a853-18f01e443586)

## Borrow Records
![image](https://github.com/user-attachments/assets/5577ba51-3ef6-4ec2-b1b6-8f4cdd827085)


## Bắt lỗi

Lỗi xảy ra khi thực hiện các thao tác thêm, sửa, xóa (trong BorrowRecordController, UserController, hoặc BookController) khiến giao diện hiển thị thông báo lỗi, nhưng dữ liệu vẫn được lưu thành công vào cơ sở dữ liệu (xác nhận sau khi tải lại trang).

![lỗi](https://github.com/user-attachments/assets/76622475-9a70-413c-96d7-af4a1880626b)


# Hướng dẫn chạy ứng dụng Spring Boot Quản lý Sách

Vào GitHub repository chứa mã nguồn dự án, nhấn nút **Code → Open with Codespaces** để tạo một môi trường làm việc trực tuyến. Sau khi Codespace khởi động, mở Terminal và thực hiện các bước sau:

1. Di chuyển vào thư mục chứa mã nguồn Spring Boot:

```bash
cd springbootApp/complete

```
2. Kiểm tra phiên bản Java đang dùng:
```
java --version

```
Nếu kết quả là Java 11, cần nâng cấp lên Java 17:

```
sudo apt-get update
sudo apt-get install openjdk-17-jdk
sudo update-alternatives --config java

```
Thiết lập biến môi trường:
```
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```
Xác nhận lại:
```
java --version

```
3. Chạy ứng dụng Spring Boot
Tại thư mục springbootApp/complete, chạy lệnh:
```
mvn spring-boot:run

```
4. Truy cập giao diện người dùng
Sau khi ứng dụng khởi động thành công, mở tab "Preview" hoặc truy cập đường dẫn.









