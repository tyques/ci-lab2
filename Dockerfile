FROM gradle:9.1.0-jdk25 AS builder
WORKDIR /app
COPY build.gradle settings.gradle /app/
RUN gradle dependencies --no-daemon
COPY src /app/src
RUN gradle assemble --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
