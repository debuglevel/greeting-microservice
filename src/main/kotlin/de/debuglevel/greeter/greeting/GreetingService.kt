package de.debuglevel.greeter.greeting

import mu.KotlinLogging
import javax.inject.Singleton

/**
 * Greets persons
 */
@Singleton
class GreetingService {
    private val logger = KotlinLogging.logger {}

    private val localizedGreetings = mapOf(
        "xx_XX" to "I don't speak your language, but I still want to say hello, %s",
        "de_DE" to "Grüß Gott, %s",
        "de_CH" to "Grüezi %s!",
        "en_GB" to "Good morning, %s.",
        "en_US" to "Howdy, %s!"
    )

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
    fun greet(name: String, language: String?): Greeting {
        logger.debug { "Greeting '$name' with language '$language'..." }

        if (name.isBlank()) {
            logger.debug { "Cannot greet a blank name" }
            throw GreetingException("Cannot greet a blank name.")
        }

        val languageKey = when {
            language.isNullOrBlank() -> localizedGreetings.keys.first()
            !localizedGreetings.containsKey(language) -> localizedGreetings.keys.first()
            else -> language
        }

        val localizedGreeting = localizedGreetings.getValue(languageKey)

        val greeting = Greeting(localizedGreeting, name)

        logger.debug { "Greeted '$name' in '$language' like '${greeting.greeting}'..." }

        return greeting
    }

    class GreetingException(message: String) : Exception(message)
}