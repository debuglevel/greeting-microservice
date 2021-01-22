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
#RUN ./gradlew build
RUN ./gradlew assemble

## GraalVM native-image
FROM oracle/graalvm-ce:20.1.0-java8 as graalvm
WORKDIR /app
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar
RUN gu install native-image
RUN java -version
RUN native-image --version
RUN native-image --no-server -cp /app/microservice.jar
RUN ls -al /app
RUN native-image --no-server --no-fallback --class-path /app/microservice.jar
RUN ls -al /app

## Final image
FROM frolvlad/alpine-glibc

# add curl for health check
RUN apk add --no-cache curl

COPY --from=graalvm /app/microservice .

# set the default port to 80
ENV MICRONAUT_SERVER_PORT 80
EXPOSE 80

# use a log appender with no timestamps as Docker logs the timestamp itself ("docker logs -t ID")
ENV LOG_APPENDER classic-stdout

HEALTHCHECK --interval=5m --timeout=5s --retries=3 --start-period=1m CMD curl --fail http://localhost/health || exit 1

CMD ["./microservice"]