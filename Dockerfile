# PRODUCTION environment

# ==STAGE 1: Build the JAR with Maven==
   # Combined image: maven + jdk21 from Eclipse Temurin
FROM maven:3.9.9-eclipse-temurin-21 AS build
   # Working directory inside the container
WORKDIR /app
   # Copy only pom.xml first (Docker layer cache optimization)
COPY pom.xml ./
   # Download dependencies offline in batch mode (no interactive prompts)
RUN mvn dependency:go-offline -B
   # Copy only the Java sources (tests are NOT included)
COPY src ./src
   # Clean and package (produces the *.jar without running tests)
RUN mvn clean package -DskipTests

# ==STAGE 2: Java application runtime==
   # JRE-only image to keep the final container small
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
   # Copy the *.jar produced in the build stage
COPY --from=build /app/target/*.jar app.jar
   # Activate the 'prod' Spring profile by default (Render and docker-compose rely on this)
ENV SPRING_PROFILES_ACTIVE=prod
   # Production HTTP port (matches application-prod.yml -> server.port: 10000)
EXPOSE 10000
   # Entry point when the container starts on the host: java -jar app.jar
CMD ["java", "-jar", "app.jar"]


# ------------------------------------- COMMANDS ----------------------------------------------------------
# Build the image (WARNING: the trailing dot is required)
#> docker build -t devops:latest .

# Create and start the container from the image
#> docker run -d --name devops1 -p 10000:10000 devops:latest

# Start an existing container
#> docker start devops1
