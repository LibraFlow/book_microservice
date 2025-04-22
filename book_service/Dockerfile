FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080
CMD ["java", "-jar", "app.jar"] 