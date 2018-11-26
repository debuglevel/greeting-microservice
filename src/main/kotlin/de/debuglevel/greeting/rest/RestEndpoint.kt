package de.debuglevel.greeting.rest

import de.debuglevel.greeting.rest.greeting.GreetingController
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

        // publish following paths on e.g. /v1/greetings/
        apiVersion("1")
        {
            path("/greetings") {
                get("/:name") {
                    val name = params(":name")
                    logger.info("Got request to greeting '$name' on API v1")
                    "Hello from API v1, $name!"
                }
            }
        }

        // publish following paths on e.g. /v2/greetings/ and /greetings/ (as "default" is true)
        apiVersion("2", true)
        {
            path("/greetings") {
                //get("/", "text/html", GreetingController.getListHtml())
                get("/", "application/json", GreetingController.getList())
                //post("/", function = GreetingController.postOne())

                path("/:name") {
                    get("", "application/json", GreetingController.getOne())
                    get("/", "application/json", GreetingController.getOne())
                    //get("/", "text/html", GreetingController.getOneHtml())
                }
            }
        }
    }
}
