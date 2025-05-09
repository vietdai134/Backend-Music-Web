# FROM eclipse-temurin:17-jdk-alpine
# # VOLUME /tmp
# COPY target/Music-Web-0.0.1-SNAPSHOT.jar app.jar
# # COPY cred.json /app/cred.json
# ENTRYPOINT ["java", "-jar", "/app.jar"]

# Sử dụng Maven để build ứng dụng
FROM maven:3.8.4-openjdk-17-slim AS builder

# Set working directory
WORKDIR /app

# Copy mã nguồn vào container
COPY . .

# Build ứng dụng với Maven (có thể thay bằng Gradle nếu dùng Gradle)
RUN mvn clean package -DskipTests

# Sử dụng một image JDK để chạy ứng dụng
FROM eclipse-temurin:17-jdk-alpine

# Copy file .jar đã build từ bước trước vào image
COPY --from=builder /app/target/Music-Web-0.0.1-SNAPSHOT.jar /app.jar

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]
