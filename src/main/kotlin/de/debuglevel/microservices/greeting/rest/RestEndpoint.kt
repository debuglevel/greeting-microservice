package de.debuglevel.microservices.greeting.rest

import com.google.gson.GsonBuilder
import de.debuglevel.microservices.greeting.Greeter
import de.debuglevel.microservices.utils.apiversion.apiVersion
import de.debuglevel.microservices.utils.spark.configuredPort
import de.debuglevel.microservices.utils.status.status
import mu.KotlinLogging
import spark.Spark.path
import spark.kotlin.get

/**
 * REST endpoint
 */
class RestEndpoint {
    private val logger = KotlinLogging.logger {}

    /**
     * Starts the REST endpoint to enter a listening state
     *
     * @param args parameters to be passed from main() command line
     */
    fun start(args: Array<String>) {
        logger.info("Starting...")
        configuredPort()
        status(this::class.java)

        // publish following paths on e.g. /v1/greet/
        apiVersion("1")
        {
            path("/greet") {
                get("/:name") {
                    val name = params(":name")
                    logger.info("Got request to greet '$name' on API v1")
                    "Hello from API v1, $name!"
                }
            }
        }

        // publish following paths on e.g. /v2/greet/ and /greet/ (as "default" is true)
        apiVersion("2", true)
        {
            path("/greet") {
                get("/:name") {
                    val name = params(":name")

                    logger.info("Got request to greet '$name' on API v2")

                    type(contentType = "application/json")
                    try {
                        val greeting = Greeter.greet(name)
                        GsonBuilder()
                                .setPrettyPrinting()
                                .create()
                                .toJson(greeting)
                    } catch (e: Greeter.GreetingException) {
                        logger.info("Name '$name' could not be greeted: ", e.message)
                        response.type("application/json")
                        response.status(400)
                        "{\"message\":\"name '$name' could not be greeted: ${e.message}\"}"
                    }
                }
            }
        }
    }
}
