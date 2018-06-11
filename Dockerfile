FROM openjdk:8-jdk-alpine AS builder
COPY . /src/
WORKDIR /src/
RUN ./gradlew build


FROM openjdk:8-jre-alpine
RUN mkdir /app
COPY --from=builder /src/build/libs/*-all.jar /app/greeting-microservice.jar

# set the default port to 80
ENV PORT 80
EXPOSE 80

CMD ["java", "-jar", "/app/greeting-microservice.jar"]
