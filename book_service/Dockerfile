FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar
EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-XX:+PrintCommandLineFlags -Xmx400m -XX:+UseContainerSupport -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prod"
ENTRYPOINT ["java", "-jar", "app.jar"] 