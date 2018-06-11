# Greeting Microservice Example

This is an example how to create a microservice.

## Technology used

### Basics

* Spark
  * REST microservice
  * GSON
* Kotlin
  * Logging (kotlin-logging, slf4j)
  * Tests (Junit 5)
* Gradle (with Wrapper)
  * Fat shadowed Jar

### Integration

* GitLab CI/CD using Docker images in GitLab Runner
  * save artifacts in GitLab
* Travis CI
  * deploy on GitHub Releases
  * deploy on Heroku
* Heroku
* Docker
  * Dockerfile
  * docker-compose.yml