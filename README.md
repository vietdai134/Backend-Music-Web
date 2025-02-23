# Music-Web
Clean Architecture
1. Presentation Layer (API)
Controllers (REST API Controllers).
Request (Request DTOs - dữ liệu từ client gửi lên).
Response (Response DTO - dữ liệu trả về client).
ApiExceptionHandler.java (Xử lý exception cho API).
2. Application Layer (Use Cases, DTOs, Services)
DTO (DTOs để truyền dữ liệu giữa các tầng).
Services (Interface của Service - các use case chung).
Ports : 
- In (Interface của usecases - các Use case riêng).
- Out (Interface của Repository).
UseCases (implement của usecase) .
3. Domain Layer
Entities
Enums
Exceptions
ValueObjects (Các kiểu dữ liệu bất biến)
4. Infrastructure Layer
Persistence (Xử lý lưu trữ dữ liệu):
- Repositories (implement của repository)
Services (implement của Application.Services)
Security
Config
- JpaConfig.java (cấu hình JPA, Hibernate)
- ApplicationStartup.java (Cấu hình khi ứng dụng khởi động)