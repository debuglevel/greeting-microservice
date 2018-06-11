package de.debuglevel.microservices.spark

import mu.KotlinLogging
import spark.kotlin.port

// TODO: This should be a independent jar, which can be used as a dependency from all microservices

private val logger = KotlinLogging.logger {}

/**
 * Gets the value of the PORT environment variable, or 4567 if no such environment variable is set.
 *
 * @return value of the PORT environment variable, or 4567 if no such environment variable is set.
 */
private fun getEnvironmentPort(): Int {
    // TODO: add more options to figure out a port configuration

    val processBuilder = ProcessBuilder()
    return if (processBuilder.environment()["PORT"] != null) {
        Integer.parseInt(processBuilder.environment()["PORT"])
    } else {
        4567
    }
}

/**
 * Sets the port to a configured port.
 *
 * Currently, only the environment variable "PORT" is evaluated.
 */
fun configuredPort() {
    // TODO: Spark fails silently if port is already taken. There should be thrown an exception

    val port = getEnvironmentPort()
    logger.info("Setting port to $port...")
    port(port)
    //logger.info("Setting port to $port succeeded.")
}