FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/Music-Web-0.0.1-SNAPSHOT.jar app.jar
COPY cred.json /app/cred.json
ENTRYPOINT ["java", "-jar", "/app.jar"]
