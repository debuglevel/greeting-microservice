## Building stage
#FROM openjdk:11-jdk AS builder # use OpenJDK 11 if desired
FROM openjdk:8-jdk-alpine AS builder
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
FROM oracle/graalvm-ce:1.0.0-rc13 as graalvm
WORKDIR /app
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar
RUN java -version
RUN native-image --version
RUN native-image --no-server -cp /app/microservice.jar
RUN ls -al /app
RUN native-image --no-server --no-fallback --class-path /app/microservice.jar
RUN ls -al /app

## Final image
FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /app/microservice .
CMD ["./microservice"]

