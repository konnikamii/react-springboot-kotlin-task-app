FROM openjdk:25-jdk-slim

WORKDIR /app
 
COPY . /app 

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]