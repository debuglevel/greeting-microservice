ARG OPENJDK_VERSION=17.0.2


## Building stage
# "zulu-openjdk" is Ubuntu
FROM azul/zulu-openjdk:${OPENJDK_VERSION} AS builder
#FROM azul/zulu-openjdk-debian:${OPENJDK_VERSION} AS runtime
#FROM azul/zulu-openjdk-alpine:$OPENJDK_VERSION AS builder

WORKDIR /src/

# Cache Gradle
COPY gradle /src/gradle
COPY gradlew /src/
# Run "gradle --version" to let gradle-wrapper download Gradle
RUN ./gradlew --version

# Copy source, show versions, build
COPY . /src/
RUN java -version
RUN ./gradlew --version
RUN ./gradlew build


## Final image
# "zulu-openjdk" is Ubuntu
#FROM azul/zulu-openjdk:${OPENJDK_VERSION}-jre AS runtime
#FROM azul/zulu-openjdk-debian:${OPENJDK_VERSION}-jre AS runtime
FROM azul/zulu-openjdk-alpine:${OPENJDK_VERSION}-jre AS runtime

# Add a group and a user with specified IDs
RUN addgroup -S -g 1111 appgroup && adduser -S -G appgroup -u 1111 appuser # if based on Alpine
#RUN groupadd -r -g 1111 appgroup && useradd -r -g appgroup -u 1111 --no-log-init appuser # if based on Debian/Ubuntu

# Add curl for health check
RUN apk add --no-cache curl # if based on Alpine
#RUN apt-get update && apt-get install -y curl # if based on Debian/Ubuntu

# Add /data directory with correct rights
RUN mkdir /data && chown 1111:1111 /data

WORKDIR /app
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar

# Switch to unprivileged user for following commands
USER appuser

# Set the default port to 8080
ENV MICRONAUT_SERVER_PORT 8080
EXPOSE 8080

# Use a log appender with no timestamps as Docker logs the timestamp itself (`docker logs -t ID`)
ENV LOG_APPENDER classic-stdout

# CAVEAT: interval should not be too high, as some tools (e.g. traefik) wait for a healthy state.
HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=60s CMD curl --fail http://localhost:8080/health || exit 1

# "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap" lets the JVM respect CPU and RAM limits inside a Docker container
CMD ["java", \
     "-XX:+UnlockExperimentalVMOptions", \
     "-XX:TieredStopAtLevel=1", \
     "-Dcom.sun.management.jmxremote", \
     "-jar", "/app/microservice.jar"]
