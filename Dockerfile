## Building stage
#FROM openjdk:11-jdk AS builder # use OpenJDK 11 if desired
FROM openjdk:8-jdk-alpine3.9 AS builder
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
#FROM openjdk:11-jre # use OpenJDK 11 if desired
FROM openjdk:8-jre-alpine3.9

# add curl for health check
RUN apk add --no-cache curl

WORKDIR /app
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar

# set the default port to 80
ENV MICRONAUT_SERVER_PORT 80
EXPOSE 80

# use a log appender with no timestamps as Docker logs the timestamp itself ("docker logs -t ID")
ENV LOG_APPENDER classic-stdout

HEALTHCHECK --interval=5m --timeout=5s --retries=3 --start-period=1m CMD curl --fail http://localhost/health || exit 1

# "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap" lets the JVM respect CPU and RAM limits inside a Docker container
CMD ["java", \
     "-XX:+UnlockExperimentalVMOptions", \
     "-XX:+UseCGroupMemoryLimitForHeap", \
     "-noverify", \
     "-XX:TieredStopAtLevel=1", \
     "-Dcom.sun.management.jmxremote", \
     "-jar", "/app/microservice.jar"]
