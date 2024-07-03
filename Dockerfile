FROM gradle:jdk11 AS build
COPY . .
RUN gradle clean package -DskipTests


FROM openjdk:11-jdk-slim
COPY --from=build /home/gradle/project/build/libs/HGPUserRegistration-1.0.0.jar /app/myapp.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","myapp.jar"]