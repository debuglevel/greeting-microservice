# Greeting Microservice Example

This is an example how to create a microservice in Kotlin.

## Technology used

### Basics

* Spark
  * REST microservice
  * GSON
* Kotlin
  * Logging (kotlin-logging, SLF4J)
  * Tests (JUnit 5)
* Gradle (with Wrapper)
  * fat shadowed Jar

### Integration

* Git
  * .gitignore
* GitLab CI/CD using Docker images in GitLab Runner
  * save artifacts in GitLab
  * upload Docker image to GitLab registry
* Travis CI
  * deploy on GitHub Releases
  * deploy on Heroku
* Heroku
  * Procfile
* Docker (Linux Container)
  * Dockerfile
  * docker-compose.yml
  * .dockerignore
