FROM gradle:9.1.0-jdk25 AS build
WORKDIR /app

# Сначала только описание зависимостей: слой со скачанными библиотеками
# берётся из кэша, пока не меняются build.gradle/settings.gradle
COPY build.gradle settings.gradle ./
RUN gradle resolveDependencies --no-daemon

COPY src ./src
RUN gradle bootJar --no-daemon --offline

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
# apk upgrade подтягивает исправления пакетов alpine, ещё не попавшие в базовый образ
RUN apk upgrade --no-cache && addgroup -S app && adduser -S app -G app
COPY --from=build /app/build/libs/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
