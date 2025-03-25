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

Swagger: 
<!-- http://localhost:8080/swagger-ui/index.html -->
https://localhost:8443/swagger-ui/index.html

Tạo ssl:
1. Tạo folder ssl trong resources
2. Vào thư mục ssl 
tạo chứng chỉ ký tự:
openssl req -x509 -newkey rsa:2048 -keyout private.key -out certificate.crt -days 365 -nodes
Nên đặt common name là localhost hoặc tên của domain.
Chuyển chứng chỉ sang keystore .p12:
openssl pkcs12 -export -in certificate.crt -inkey private.key -out keystore.p12 -name mycert -password pass:mypassword
3. cấu hình trong application.properties:
server.ssl.key-store-password=mypassword
server.ssl.key-alias=mycert

Tạo file json để sử dụng google drive api:
Tham khảo tại: https://youtu.be/rANfiSmyMTQ?si=hPKqqhrMmt_REmQX
