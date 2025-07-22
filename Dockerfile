# Use Eclipse Temurin JDK 17 base image (lightweight)
FROM eclipse-temurin:17-jdk-alpine

# Create a temporary volume
VOLUME /tmp

# Build argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]
