package de.debuglevel.greeter.person

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/persons")
class PersonController(private val personService: PersonService) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get a person
     * @param id ID of the person
     * @return A person
     */
    @Get("/{id}")
    fun getOne(id: Long): Person? {
        logger.debug("Called getOne($id)")
        return personService.retrieve(id)
    }

    /**
     * Create a person.
     * @param name Name of the person
     * @return A person with ID
     */
    @Post("/{name}")
    fun postOne(name: String): Person {
        logger.debug("Called postOne($name)")
        val person = Person(0, name)
        return personService.save(person)
    }

    /**
     * Get all VIPs
     * @return All VIPs
     */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/VIPs")
    fun getVIPs(): Set<Person> {
        logger.debug("Called getVIPs()")
        return setOf<Person>(
            Person(0, "Harry Potter"),
            Person(0, "Hermoine Granger"),
            Person(0, "Ronald Weasley")
        )
    }
}