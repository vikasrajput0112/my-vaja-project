# ===============================
# Stage 1: Build the application
# ===============================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /build

# Copy Maven descriptor first (for better layer caching)
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn -B clean package


# ===============================
# Stage 2: Runtime image
# ===============================
FROM eclipse-temurin:21-jre-jammy

# Create non-root user (security best practice)
RUN useradd -r -u 1001 appuser

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/target/my-vaja-project-1.0.0.jar app.jar

# Set ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose application port (optional, change if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
