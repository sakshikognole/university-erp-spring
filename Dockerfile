# ── Stage 1: Build with Gradle ─────────────────────────────
# Uses the official Gradle+JDK17 image — no local Gradle install needed.
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy build scripts first (layer-cached if unchanged)
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Download dependencies separately (improves layer caching)
RUN gradle dependencies --no-daemon || true

# Copy source and build fat jar
COPY src src
RUN gradle bootJar --no-daemon

# ── Stage 2: Minimal production runtime ────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from stage 1
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Pass Spring properties via environment variables (set in docker-compose)
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
