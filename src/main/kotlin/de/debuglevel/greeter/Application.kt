package de.debuglevel.greeter

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.Micronaut
import mu.KotlinLogging

/**
 * Application entry point.
 *
 * Starts the Micronaut server
 *
 * @param args parameters from the command line call
 */
object Application {
    private val logger = KotlinLogging.logger {}

    lateinit var applicationContext: ApplicationContext

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info { "Starting up..." }
        applicationContext = Micronaut.run(Application.javaClass)

        // TODO: how to do API versioning? (or do it at all?)
        // TODO: do configuration with some Micronaut provided stuff instead of natpryce.konfig?
        // TODO: prettier JSON output?
        // TODO: create a README.md which is usable as a template
        // TODO: retain parameter names via compileJava.options.compilerArgs += '-parameters' or/and? https://docs.micronaut.io/latest/guide/index.html#kotlinretainparamnames

    }
}


