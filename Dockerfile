FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp
WORKDIR /app
# Cài Maven
RUN apk add --no-cache maven
# Copy mã nguồn
COPY . .
# Build file JAR
RUN mvn clean package -DskipTests
COPY target/Music-Web-0.0.1-SNAPSHOT.jar app.jar
# COPY cred.json /app/cred.json
ENTRYPOINT ["java", "-jar", "/app.jar"]
