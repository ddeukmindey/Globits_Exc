## PHẦN 1: SPRING BOOT

### Kiến thức cần nắm
- [ ] Cấu hình trong file `application.properties`.
- [ ] Sử dụng `@RestController`, cấu hình các HTTP methods (GET, POST, PUT, DELETE).
- [ ] Xây dựng tầng nghiệp vụ với `@Service` và cơ chế tiêm phụ thuộc bằng `@Autowired`.
- [ ] Sử dụng DTO (Data Transfer Object) để truyền nhận dữ liệu.
- [ ] Các Annotation xử lý request:
  - `@RequestBody` (nhận body request)
  - `@RequestParam` (lấy query parameters)
  - `@PathVariable` (lấy param từ URL path)

## PHẦN 2: DATABASE - HIBERNATE

### 1. Cơ bản & CRUD Entity đơn
- [ ] **Kiến thức:**
  - [ ] Tìm hiểu cơ chế hoạt động của Hibernate / JPA.
  - [ ] Cách ánh xạ entity vào bảng cơ sở dữ liệu.
- [ ] **2. Tạo Model Country:**
  - [ ] Entity `Country` ánh xạ tới bảng `countries` gồm các trường:
    - `id`: Khóa chính
    - `name`: Kiểu `string`
    - `code`: Kiểu `string`
    - `description`: Kiểu `text`
- [ ] **3. Viết CRUD cho Country:**
  - [ ] Viết đầy đủ API: Thêm, Sửa, Xóa, Lấy danh sách / chi tiết.

### 2. Các loại quan hệ (Relationships)
- [ ] **Kiến thức:**
  - [ ] Tìm hiểu các loại quan hệ: One-to-One, One-to-Many, Many-to-Many.
  - [ ] Cách cấu hình foreign key (`@JoinColumn`), CascadeType, FetchType (`LAZY` / `EAGER`).

### 3. Quan hệ One-to-One (1 - 1)
- [ ] **4. Tạo Model quan hệ 1 - 1:**
  - [ ] Model `User` gồm: `email`, `password`, `is_active`.
  - [ ] Model `Person` gồm: `full_name`, `gender`, `birthdate`, `phone_number`, `address`.
  - [ ] Thiết lập quan hệ One-to-One giữa `User` và `Person`.
- [ ] **5. Viết CRUD cho User và Person:**
  - [ ] Xây dựng API CRUD cho cả 2 entity kèm liên kết dữ liệu giữa hai bên.

### 4. Quan hệ One-to-Many (1 - N)
- [ ] **6. Tạo Model quan hệ 1 - N:**
  - [ ] Model `Company` gồm: `name`, `code`, `address`.
  - [ ] Thiết lập quan hệ One-to-Many giữa `Company` và `Person` (Một công ty có nhiều nhân sự).
- [ ] **7. Viết CRUD Company & Cập nhật Person:**
  - [ ] Viết API CRUD cho `Company`.
  - [ ] Sửa lại CRUD của `Person`, cho phép chọn/gán `Company`.

### 5. Quan hệ Many-to-Many (N - N)
- [ ] **8. Tạo Model quan hệ N - N:**
  - [ ] Model `Role` gồm: `role`, `description`.
  - [ ] Thiết lập quan hệ Many-to-Many giữa `User` và `Role` (bảng trung gian qua `@JoinTable`).
- [ ] **9. Viết CRUD Role & Cập nhật User:**
  - [ ] Viết API CRUD cho `Role`.
  - [ ] Sửa lại CRUD của `User`, cho phép phân quyền (một `User` có nhiều `Role`).

---

## PHẦN 3: BÀI TẬP NÂNG CAO

### 1. CRUD cho Department (Phòng ban)
- [ ] **10. Tạo Model Department:**
  - [ ] Các trường: `code`, `name`, `parent_id`, `company_id`.
  - [ ] Mỗi phòng ban sẽ thuộc một `Company` (`company_id`).
  - [ ] Mỗi phòng ban có thể chọn phòng ban cha (`parent_id`).
- [ ] **11. Viết CRUD cho Department:**
  - [ ] Làm trong màn hình chỉnh sửa `Company`.
  - [ ] Viết API hiển thị danh sách các `Department` thuộc `Company` đó.

### 2. CRUD cho Project (Dự án)
- [ ] **12. Tạo Model & CRUD Project:**
  - [ ] Model `Project` gồm: `code`, `name`, `description`, `company_id`.
  - [ ] Quan hệ Many-to-Many giữa `Project` và `Person`.
  - [ ] Xây dựng API CRUD cho `Project`.
  - [ ] Khi create/edit `Project`: Có thể chọn `Company`, sau khi chọn `Company` có thể chọn nhiều `Person` (`projectPerson`) tham gia vào dự án từ danh sách `Person` thuộc chính `Company` vừa chọn.
  - [ ] Làm phân trang (Pagination) cho danh sách `Project`.
  - [ ] Bổ sung phân trang tương tự cho danh sách `Company` và `Person`.

### 3. CRUD cho Task (Công việc)
- [ ] **13. CRUD (có phân trang) cho bảng Task:**
  - [ ] Model `Task` gồm các trường:
    - `project_id`: Thuộc dự án nào.
    - `person_id`: Ai làm công việc này.
    - `start_time`: Ngày bắt đầu công việc.
    - `end_time`: Ngày kết thúc công việc.
    - `priority`: Mức độ ưu tiên (`1: Cao`, `2: Trung bình`, `3: Thấp`).
    - `name`: Tên công việc.
    - `description`: Mô tả công việc.
    - `status`: Trạng thái công việc (`1: Mới tạo`, `2: Đang làm`, `3: Hoàn thành`, `4: Tạm hoãn`).
  - [ ] Xây dựng API CRUD đầy đủ có hỗ trợ phân trang cho `Task`.
- [ ] **14. Lọc & Tìm kiếm Task:**
  - [ ] API danh sách `Task` cần hỗ trợ lọc theo: `Company`, `Project`, `Person`, `Status`, `Priority`.
  - [ ] Hỗ trợ tìm kiếm theo `name`.

### 4. Xuất Excel
- [ ] **15. Viết API xuất Excel cho Task:**
  - [ ] Xuất excel gồm các cột: `Project`, `Description`, `Start Time`, `End Time`, `Priority`, `Status`, `Person`.
  - [ ] Khi xuất excel sẽ không phân trang và xuất toàn bộ dữ liệu theo bộ lọc.

### 5. Upload File
- [ ] **16. Thêm tính năng Avatar cho Person:**
  - [ ] Bảng `Person` thêm một trường `avatar` (`string`) lưu path ảnh.
  - [ ] Xây dựng tính năng/API cho phép upload ảnh avatar cho `Person`.