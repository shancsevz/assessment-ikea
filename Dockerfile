# Step 1: Use an official OpenJDK base image from Docker Hub
FROM registry.access.redhat.com/ubi8/openjdk-17:1.19

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the Spring Boot JAR file into the container
COPY target/java-code-assignment-1.0.0-SNAPSHOT.jar /app/java-code-assignment-1.0.0-SNAPSHOT.jar

# Step 4: Expose the port your application runs on
EXPOSE 8080

# Step 5: Define the command to run your Spring Boot application
CMD ["java", "-jar", "/app/java-code-assignment-1.0.0-SNAPSHOT.jar"]