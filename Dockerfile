# Use a lightweight Java 17 runtime environment
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the containe
WORKDIR /station

# Copy the compiled JAR file from your target folder into the container
# We use a wildcard so it grabs the JAR regardless of the exact version number
COPY target/*.jar app.jar

# Expose the custom port the station uses
EXPOSE 8084

# The command to start the application when the container launches
ENTRYPOINT ["java", "-jar", "app.jar"]