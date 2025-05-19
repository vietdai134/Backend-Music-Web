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

Demo image:
Login, Sign up
![image](https://github.com/user-attachments/assets/2acc7439-d95b-4cb0-9cb2-ee98225e0280)
![image](https://github.com/user-attachments/assets/20efd70d-801f-4b9d-93da-8437ed5e48c0)

Forget password
![image](https://github.com/user-attachments/assets/660cb7a4-0fd4-462f-9d2a-00deca5feb4b)
![image](https://github.com/user-attachments/assets/b3597d24-b70f-4c83-b400-d5bddf269ab4)

Giao diện chính
![image](https://github.com/user-attachments/assets/baef5006-f79e-4c01-9d5c-16a1aeb5b9e4)
![image](https://github.com/user-attachments/assets/ae00c85c-38cb-424c-9a4d-d4cddd2fc0a9)
![image](https://github.com/user-attachments/assets/5962e88d-a2d9-4619-84e7-1fa76579881d)
![image](https://github.com/user-attachments/assets/1a51c35b-e3ee-4a97-b4ee-17d896eeeefc)
![image](https://github.com/user-attachments/assets/9abc2767-7693-4a15-838e-0914be700506)
![image](https://github.com/user-attachments/assets/f92ed05b-25a2-4ceb-953b-fed700918d03)
![image](https://github.com/user-attachments/assets/6dfbef36-a990-4267-bf00-2481b333298b)
![image](https://github.com/user-attachments/assets/d1ba325e-674e-4783-bf7a-3f7148617d49)
![image](https://github.com/user-attachments/assets/87924a34-44d7-4306-860a-3fd35af3948d)

Profile
![image](https://github.com/user-attachments/assets/8349bf55-cfd6-432e-a422-9b5ff73f3292)

Giao diện Admin
![image](https://github.com/user-attachments/assets/629b73c2-c177-4a62-8df4-05c5d7504dc4)
![image](https://github.com/user-attachments/assets/14b85e45-c15d-4ece-ba85-af2a7f120f8c)
![image](https://github.com/user-attachments/assets/f59bae2b-17c9-4ccd-9636-f4ebbfc43711)
![image](https://github.com/user-attachments/assets/4cf20234-4c1e-4128-bb32-530f1705d9fc)
![image](https://github.com/user-attachments/assets/af97c74e-2d1c-4b1b-a165-5752caca586b)
![image](https://github.com/user-attachments/assets/1a7a47d7-e338-4b68-b716-3e94c5fca8c3)

Link Video Demo: https://youtu.be/7VgeuqOVQMQ?si=EWZAyng7l2-3RJaq

Link frontend: https://github.com/vietdai134/Frontend-Music-Web.git
Link Backend Lyric, AI: https://github.com/vietdai134/Lyrics-Music-Web.git
