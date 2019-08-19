package de.debuglevel.greeter.greeting

import io.micronaut.core.version.annotation.Version
import io.micronaut.http.MediaType
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
     * Get a greeting for a person.
     * @param name Name of the person to greet
     * @return A greeting for a person
     */
    @Version("1")
    @Get("/{name}", produces = [MediaType.TEXT_PLAIN])
    fun getOneV1(name: String): String {
        logger.debug("Called getOneV1($name)")
        return "Hello, $name"
    }

    /**
     * Get a greeting for a person. If given, the greeting is localized in a language.
     * @param name Name of the person to greet
     * @param language The language to greet the person in
     * @return A greeting for a person in a given language
     */
    @Version("2")
    @Get("/{name}{?language}")
//    fun getOneV2(@NotNull name: String, @Nullable language: String?): GreetingDTO {
    fun getOneV2(name: String, language: String?): Greeting {
        logger.debug("Called getOneV2($name, $language)")
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