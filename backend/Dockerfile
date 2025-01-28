FROM openjdk:23-jdk-slim

WORKDIR /app

COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./

COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test --no-daemon

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]