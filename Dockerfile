# syntax=docker/dockerfile:1

# --- Build stage: compile the Spring Boot server into an executable jar ---
FROM eclipse-temurin:24-jdk AS build
WORKDIR /workspace

# Copy the whole multi-module project (build context is the repo root).
COPY . .

# Normalize the wrapper's line endings (it may be checked out with CRLF on
# Windows), make it executable, then build only the server boot jar.
RUN sed -i 's/\r$//' gradlew \
    && chmod +x gradlew \
    && ./gradlew --no-daemon :server:bootJar -x test

# --- Runtime stage: run the jar on a clean JDK image ---
FROM eclipse-temurin:24-jdk AS runtime
WORKDIR /app

# bootJar (invoked alone) produces a single executable jar under build/libs.
COPY --from=build /workspace/server/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
