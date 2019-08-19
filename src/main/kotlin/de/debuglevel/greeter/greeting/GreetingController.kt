package de.debuglevel.greeter.greeting

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import mu.KotlinLogging

/**
 * Generates greetings for persons
 */
@Controller("/greetings")
class GreetingController(private val greetingService: GreetingService) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get a greeting for a person. If given, the greeting is localized in a language.
     * @param name Name of the person to greet
     * @param language The language to greet the person in
     * @return A greeting for a person in a given language
     */
    @Get("/{name}{?language}")
//    fun getOne(@NotNull name: String, @Nullable language: String?): GreetingDTO {
    fun getOne(name: String, language: String?): Greeting {
        logger.debug("Called getOne($name, $language)")
        return greetingService.greet(name, language)
    }

    /**
     * Gets some greetings.
     * @return Some greetings
     */
    @Get("/")
    fun getList(): Set<Greeting> {
        logger.debug("Called getList()")
        val greetings = setOf<Greeting>(
            Greeting("Servusla, %s", "Mozart"),
            Greeting("Wie schaut's, %s?", "Beethoven"),
            Greeting("Moin %s", "Haydn")
        )

        return greetings
    }
}