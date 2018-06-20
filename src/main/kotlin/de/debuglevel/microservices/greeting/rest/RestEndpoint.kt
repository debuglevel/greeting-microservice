package de.debuglevel.microservices.greeting.rest

import com.google.gson.GsonBuilder
import de.debuglevel.microservices.greeting.Greeter
import de.debuglevel.microservices.utils.spark.configuredPort
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

        path("/greet") {
            get("/:name") {
                val name = params(":name")

                logger.info("Got request to greet '$name'")

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
