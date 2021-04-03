## Building stage
FROM azul/zulu-openjdk-alpine:11.0.7 AS builder
WORKDIR /src/

# cache gradle
COPY gradle /src/gradle
COPY gradlew /src/
# run "gradle --version" to let gradle-wrapper download gradle
RUN ./gradlew --version

# build source
COPY . /src/
RUN ./gradlew build

## Final image
FROM azul/zulu-openjdk-alpine:11.0.7-jre

# add a group and an user with specified IDs
RUN addgroup -S -g 1111 appgroup && adduser -S -G appgroup -u 1111 appuser 
#RUN groupadd -r -g 1111 appgroup && useradd -r -g appgroup -u 1111 --no-log-init appuser # if based on Debian/Ubuntu

# add curl for health check
RUN apk add --no-cache curl
#RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* # if based on Debian/Ubuntu

WORKDIR /app
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar

# switch to unprivileged user for following commands
USER appuser

# set the default port to 8080
ENV MICRONAUT_SERVER_PORT 8080
EXPOSE 8080

# use a log appender with no timestamps as Docker logs the timestamp itself ("docker logs -t ID")
ENV LOG_APPENDER classic-stdout

HEALTHCHECK --interval=5m --timeout=5s --retries=3 --start-period=1m CMD curl --fail http://localhost/health || exit 1

# "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap" lets the JVM respect CPU and RAM limits inside a Docker container
CMD ["java", \
     "-XX:+UnlockExperimentalVMOptions", \
     "-XX:+UseContainerSupport", \
     "-noverify", \
     "-XX:TieredStopAtLevel=1", \
     "-Dcom.sun.management.jmxremote", \
     "-jar", "/app/microservice.jar"]
