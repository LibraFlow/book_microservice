FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar

ENV PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:postgresql:///${INSTANCE_CONNECTION_NAME}?cloudSqlInstance=${INSTANCE_CONNECTION_NAME}&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=user3&password=password3 -Dspring.datasource.username=user3 -Dspring.datasource.password=password3 -Dspring.datasource.driver-class-name=org.postgresql.Driver -Dspring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect -Dspring.jpa.hibernate.ddl-auto=update -Dlogging.level.root=DEBUG -Dlogging.level.org.springframework=DEBUG -Dlogging.level.com.zaxxer.hikari=DEBUG -Dlogging.level.org.hibernate.SQL=DEBUG"
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Create startup script
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'echo "Starting application with JAVA_OPTS: $JAVA_OPTS"' >> /app/start.sh && \
    echo 'exec java $JAVA_OPTS -jar app.jar' >> /app/start.sh && \
    chmod +x /app/start.sh

CMD ["/app/start.sh"] 