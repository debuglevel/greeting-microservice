ARG OPENJDK_VERSION=17.0.2

## Building stage
FROM azul/zulu-openjdk-alpine:$OPENJDK_VERSION AS builder
WORKDIR /src/

# Add glibc for gRPC protoc as Alpine uses musl instead
ARG GLIBC_VERSION=2.34-r0
RUN wget -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && \
  wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-$GLIBC_VERSION.apk && \
  apk add glibc-$GLIBC_VERSION.apk

# Cache Gradle
COPY gradle /src/gradle
COPY gradlew /src/
# Run "gradle --version" to let gradle-wrapper download Gradle
RUN ./gradlew --version

# Build source
COPY . /src/
#RUN ./gradlew build
RUN ./gradlew assemble

## GraalVM native-image
FROM debian as graalvm-builder

RUN apt-get update && apt-get install -y bash curl git zip gcc build-essential libz-dev zlib1g-dev && curl -s "https://get.sdkman.io" | bash
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk version"

WORKDIR /app
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 20.3.0.r8-grl"
ENV JAVA_HOME="/root/.sdkman/candidates/java/current"
ENV PATH="$JAVA_HOME/bin:$PATH"
RUN bash -c "/root/.sdkman/candidates/java/current/bin/gu install native-image"


COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar
RUN java -version
RUN native-image --version
RUN native-image --no-server -H:-DeadlockWatchdogExitOnTimeout -H:DeadlockWatchdogInterval=0 -jar /app/microservice.jar
RUN ls -al /app
#RUN native-image --no-server --no-fallback --class-path /app/microservice.jar
RUN ls -al /app

## Final image
FROM azul/zulu-openjdk-alpine:${OPENJDK_VERSION}-jre AS runtime

# Add a group and a user with specified IDs
RUN addgroup -S -g 1111 appgroup && adduser -S -G appgroup -u 1111 appuser
#RUN groupadd -r -g 1111 appgroup && useradd -r -g appgroup -u 1111 --no-log-init appuser # if based on Debian/Ubuntu

# Add curl for health check
RUN apk add --no-cache curl
#RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* # if based on Debian/Ubuntu

# Add /data directory with correct rights
RUN mkdir /data && chown 1111:1111 /data

WORKDIR /app
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar

# Switch to unprivileged user for following commands
USER appuser

# add openjdk8 as the image above needs it as a fallback for now
#RUN apk add openjdk8
#ENV JAVA_HOME="/usr/lib/jvm/default-vm/"

COPY --from=graalvm-builder /app/microservice .


# Set the default port to 8080
ENV MICRONAUT_SERVER_PORT 8080
EXPOSE 8080

# Use a log appender with no timestamps as Docker logs the timestamp itself (`docker logs -t ID`)
ENV LOG_APPENDER classic-stdout

HEALTHCHECK --interval=5m --timeout=5s --retries=3 --start-period=1m CMD curl --fail http://localhost:8080/health || exit 1

CMD ["./microservice"]