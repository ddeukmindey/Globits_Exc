# Lộ trình và Bài tập thực hành Java Spring Boot

## 1. Setup môi trường (Bài tập 0)
*   [ ] Cài đặt **MySQL 8** (Sử dụng MySQL Installer).
*   [ ] Cài đặt **Java JDK 17**.
*   [ ] Cài đặt IDE: **Eclipse**, **IntelliJ**, hoặc **VS Code**.
*   [ ] Cài đặt **Postman**.

## 2. Spring Boot Basic
*   [ ] **Bài tập 1.1:** Tạo một project Spring Boot.
*   [ ] **Bài tập 1.2:** Viết API "Hello world" và sử dụng Postman để gọi API này (kết hợp tìm hiểu Spring Boot Application.Properties).

## 3. Database & Hibernate
*   [ ] **Bài tập 2:** Tạo domain `Country` (table: `countries`).
    *   Các trường: `id` (PK), `name` (String), `code` (String), `description` (Text).
*   [ ] **Bài tập 3:** Viết các API CRUD (Create, Read, Update, Delete) cho `Country`. Lập trình theo mô hình Controller - Service - Repository.
*   [ ] **Bài tập 4:** Tạo 2 model `User` và `Person` có quan hệ One-to-One.
    *   `Person`: `full_name`, `gender`, `birthdate`, `phone_number`, `address`.
    *   `User`: `email`, `password`, `is_active`.
*   [ ] **Bài tập 5:** Viết API CRUD cho `User` và `Person`.
*   [ ] **Bài tập 6:** Tạo model `Company` có quan hệ One-to-Many với `Person`.
    *   Các trường của `Company`: `name`, `code`, `address`.
*   [ ] **Bài tập 7:** Viết API CRUD cho `Company`. Sửa lại CRUD của `Person` để cho phép chọn `Company` cho `Person`.
*   [ ] **Bài tập 8:** Tạo Model `Role` có quan hệ Many-to-Many với `User`.
    *   Các trường của `Role`: `role`, `description`.
*   [ ] **Bài tập 9:** Viết API CRUD cho `Role`. Sửa lại CRUD cho `User` để cho phép phân quyền (Mỗi `User` được chọn nhiều `Role`).

## 4. Bài tập nâng cao
*   [ ] **Bài tập 10:** Tạo model `Department` (Phòng ban).
    *   Các trường: `code`, `name`, `parent_id`, `company_id`.
    *   Mỗi phòng ban thuộc một `Company` và có thể chọn phòng ban cha (`parent_id`).
*   [ ] **Bài tập 11:** Viết API CRUD cho `Department` (tích hợp trong màn hình chỉnh sửa Company) và API hiển thị danh sách các `Department` thuộc `Company` đó.
*   [ ] **Bài tập 12:** Tạo model `Project` (Dự án) có quan hệ Many-to-Many với `Person`.
    *   Các trường: `code`, `name`, `description`, `company_id`.
    *   Viết API CRUD và phân trang cho `Project`. Khi Create/Edit dự án, cho phép chọn `Company`, sau đó chọn nhiều `Person` thuộc Company đó tham gia.
    *   Bổ sung tính năng phân trang cho `Company` và `Person`.
*   [ ] **Bài tập 13:** Viết API CRUD (có phân trang) cho bảng `Task` (Công việc).
    *   Các trường: `project_id`, `person_id`, `start_time`, `end_time`, `priority` (1: Cao, 2: Trung bình, 3: Thấp), `name`, `description`, `status` (1: Mới tạo, 2: Đang làm, 3: Hoàn thành, 4: Tạm hoãn).
*   [ ] **Bài tập 14:** Viết API danh sách `Task` cần lọc theo `Company`, `Project`, `Person`, `Status`, `Priority` và tìm kiếm theo tên.
*   [ ] **Bài tập 15:** Viết API xuất Excel cho `Task`.
    *   Xuất toàn bộ dữ liệu (không phân trang).
    *   Các cột bao gồm: Project, Description, Start Time, End Time, Priority, Status, Person.
*   [ ] **Bài tập 16:** Bổ sung upload file.
    *   Bảng `Person` thêm trường `avatar` (String) lưu đường dẫn ảnh.
    *   Viết API cho phép upload ảnh đại diện cho `Person`.