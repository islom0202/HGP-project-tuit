# Use the Gradle image with JDK 17 to build the application
FROM gradle:jdk17 AS build
COPY . .
RUN gradle clean package -DskipTests

# Use a slim JDK 17 image for the runtime environment
FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/build/libs/*.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
