FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar

ENV PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 