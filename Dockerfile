FROM eclipse-temurin:21
WORKDIR /app
RUN mkdir -p /app/logs
COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
