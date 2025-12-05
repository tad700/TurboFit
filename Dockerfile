# Stage 1: Build the runnable JAR file
# Use a specific Maven/JDK image for building
FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml first to download dependencies (improves caching)
COPY pom.xml .
RUN mvn dependency:go-offline

ENV DB_URL=jdbc:postgresql://localhost:5432/TurboFitDB
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=200220

# Copy the source code and build the final JAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Create a minimal runtime image ---

# Use the JRE (Java Runtime Environment) for a smaller, more secure final image
FROM  eclipse-temurin:21-jre

WORKDIR /app

# CRITICAL FIX: The COPY command needs a destination file name.
# Also, ensure the JAR file name matches your Spring Boot artifact name.
COPY --from=build /app/target/TurboFit-0.0.1-SNAPSHOT.jar .

# Expose the port Spring Boot runs on (default is 8080)
EXPOSE 8080

# CRITICAL FIX: Define the command to run the application using the copied JAR
ENTRYPOINT ["java", "-jar", "TurboFit-0.0.1-SNAPSHOT.jar"]
