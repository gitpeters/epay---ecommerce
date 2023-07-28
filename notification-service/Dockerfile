FROM openjdk:17-jdk-slim
# Set the working directory inside the container
WORKDIR /app
ENV PORT 8085
EXPOSE 8085
# Copy the Spring Boot application JAR file into the container
COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/epay-notification-docker.jar
# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "epay-notification-docker.jar"]