FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar

ENV PORT=8082
ENV SPRING_PROFILES_ACTIVE=prod
ENV INSTANCE_CONNECTION_NAME=libraflow-457510:europe-west4:book-service-db
ENV SPRING_DATASOURCE_USERNAME=user3
ENV SPRING_DATASOURCE_PASSWORD=password3
ENV SPRING_DATASOURCE_DATABASE=db3

EXPOSE 8082

CMD ["java", "-Xmx512m", "-Xms256m", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"] 