package de.debuglevel.greeter.greeting

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging

/**
 * Generates greetings for persons
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/greetings")
@Tag(name = "greetings")
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
    fun getOneGreeting(name: String, language: String?): Greeting {
        logger.debug("Called getOneGreeting($name, $language)")
        return greetingService.greet(name, language)
    }

    /**
     * Get a greeting for a person. If given, the greeting is localized in a language.
     * @return A greeting for a person in a given language
     */
    @Post("/")
    fun postOneGreeting(greetingRequest: GreetingRequest): Greeting {
        logger.debug("Called postOneGreeting($greetingRequest)")
        return greetingService.greet(greetingRequest.name, greetingRequest.language)
    }

    /**
     * Gets some greetings.
     * @return Some greetings
     */
    @Get("/")
    fun getAllGreetings(): Set<Greeting> {
        logger.debug("Called getAllGreetings()")
        val greetings = setOf<Greeting>(
            Greeting("Servusla, %s", "Mozart"),
            Greeting("Wie schaut's, %s?", "Beethoven"),
            Greeting("Moin %s", "Haydn")
        )

        return greetings
    }
}