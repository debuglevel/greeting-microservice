<!--- some badges to display on the GitHub page -->
![Travis (.org)](https://img.shields.io/travis/debuglevel/greeting-microservice?label=Travis%20build)
![Gitlab pipeline status](https://img.shields.io/gitlab/pipeline/debuglevel/greeting-microservice?label=GitLab%20build)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/debuglevel/greeting-microservice?sort=semver)
![GitHub](https://img.shields.io/github/license/debuglevel/greeting-microservice)

# Greeter Microservice
This is a simple REST microservice to greet people.

Of course, this is rather a template than a useful microservice 😉. It serves as a template to create a REST microservice using [Micronaut](https://micronaut.io) (with [Data](https://github.com/micronaut-projects/micronaut-data), [Security](https://github.com/micronaut-projects/micronaut-security), [JMX Management](https://github.com/micronaut-projects/micronaut-jmx), [OpenAPI/Swagger](https://github.com/micronaut-projects/micronaut-openapi)), written in [Kotlin](https://kotlinlang.org/), built with [Gradle](https://gradle.org/) (producing an executable fat jar, supporting Gradle's build scan, Researchgate's release plugin, and a dependency update checker), tested with [JUnit 5](https://junit.org/junit5/) and versioned with [git](https://git-scm.com/) (with an unseful `.gitignore`). For builds and deployment, [Docker](https://www.docker.com) (i.e. `Dockerfile`, `docker-compose.yml` and an useful `.dockerignore`) can be used, which is well integrated into [GitLab](https://gitlab.com/) (i.e. `.gitlab-ci.yml`; deploys Docker image on GitLab registry) CI/CD. There is also integration with [Travis](https://travis-ci.org/) (i.e. `.travis.yml`), which deploys on deploys on [Heroku](https://www.heroku.com/) (i.e. `Procfile`) and GitHub Releases.

## Remarks
If you fork this repository to create your own application from this template
* remove all Git tags
```
# See https://stackoverflow.com/questions/44702757/how-to-remove-all-git-origin-and-local-tags
$ git tag -d $(git tag -l) && git fetch && git push origin --delete $(git tag -l) && git tag -d $(git tag -l)
```
* and reset your `version`to `version=0.0.1-SNAPSHOT` in `gradle.proprties`

# HTTP API
Analyses have to be POSTed first to the microservice before you can GET their results. It is assumed that the R script places its results in a "output" directory.

## Swagger / OpenAPI
There is an OpenAPI (former: Swagger) specification created, which is available at <http://localhost:8080/swagger/greeter-microservice-0.1.0.yml> (or somewhere in the jar file). It can easily be pasted into the [Swagger Editor](https://editor.swagger.io) which provides a live demo for [Swagger UI](https://swagger.io/tools/swagger-ui/), but also offers to create client libraries via [Swagger Codegen](https://swagger.io/tools/swagger-codegen/).

## Get greeting
To get an appropriate greeting for a person, send a GET request to the service:
```
$ curl -X GET -H "Content-Type: application/json" -H "Accept: application/json" http://localhost/greetings/Johnny%20Knoxville
{
  "greeting" : "You did not provide a language, but I'll try english: Hello, Johnny Knoxville!"
}
```

You can also define a language as query parameter:
```
$ curl -X GET -H "Content-Type: application/json" -H "Accept: application/json" http://localhost/greetings/Johnny%20Knoxville?language=de_DE
{
  "greeting" : "Grüß Gott, Johnny Knoxville"
}
```

## Add greeting
In this example, a greeting can also be POSTed. This way, the payload is transferred in the body as JSON (which is often a better idea than putting it in the URL or parameters, due to URL encoding issues).
```
$ curl -X POST -d '{"name":"Max", "language":"de_DE"}' -H "Content-Type: application/json" -H "Accept: application/json" http://localhost/greetings/
```

# Configuration
There is a `application.yml` included in the jar file. Its content can be modified and saved as a separate `application.yml` on the level of the jar file. Configuration can also be applied via the other supported ways of Micronaut (see <https://docs.micronaut.io/latest/guide/index.html#config>). For Docker, the configuration via environment variables is the most interesting one (see `docker-compose.yml`).
