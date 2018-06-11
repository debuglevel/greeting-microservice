package de.debuglevel.microservices.greeting

import mu.KotlinLogging

/**
 * Greets persons
 */
object Greeter {
    private val logger = KotlinLogging.logger {}

    /**
     * Greets a person.
     *
     * A name like `Mozart` gets greeted like `Hello, Mozart!`.
     *
     * @param name a (valid) name which should be greeted
     * @throws GreetingException thrown if the given name is invalid (i.e. blank)
     * @return a greeting
     */
    @Throws(GreetingException::class)
    fun greet(name: String): Greeting {
        logger.debug { "Greeting '$name'..." }

        if (name.isNullOrBlank()) {
            throw GreetingException("Cannot greet a blank name.")
        }

        val greeting = Greeting(name)

        logger.debug { "Greeted '$name' like '${greeting.greeting}'..." }

        return greeting
    }

    /**
     * A greeting
     *
     * @param name name of the person to greet
     * @constructor the `name` field is annotated with `@Transient` so that it is excluded from GSON serialization
     */
    data class Greeting(@Transient private val name: String)
    {
        val greeting: String = "Hello, $name!"
    }

    class GreetingException(message: String) : Exception(message)
}