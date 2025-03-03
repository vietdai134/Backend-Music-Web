# Music-Web
Clean Architecture
1. Presentation Layer (API)
Controllers (REST API Controllers).
Request (Request DTOs - dữ liệu từ client gửi lên).
Response (Response DTO - dữ liệu trả về client).
ApiExceptionHandler.java (Xử lý exception cho API).
2. Application Layer (Use Cases, DTOs, Services)
Mapper
DTO (DTOs để truyền dữ liệu giữa các tầng).
Ports : 
- In (Interface của Services - các Use case chung và riêng).
- Out (Interface của Repository).
Services (implement của services) .
3. Domain Layer
Entities
Enums
Exceptions
ValueObjects (Các kiểu dữ liệu bất biến)
4. Infrastructure Layer
Persistence (Xử lý lưu trữ dữ liệu):
- Repositories (implement của repository)
Security
Config
- JpaConfig.java (cấu hình JPA, Hibernate)
- ApplicationStartup.java (Cấu hình khi ứng dụng khởi động)

Swagger: http://localhost:8080/swagger-ui/index.html