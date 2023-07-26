FROM openjdk:17-jdk-slim
# Set the working directory inside the container
WORKDIR /app
ENV PORT 8081
EXPOSE 8081
# Copy the Spring Boot application JAR file into the container
COPY target/*.jar /app/epay-docker.jar
# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "epay-docker.jar"]