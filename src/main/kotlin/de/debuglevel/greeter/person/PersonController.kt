package de.debuglevel.greeter.person

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging
import java.util.*

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/persons")
class PersonController(private val personService: PersonService) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get a person
     * @param uuid ID of the person
     * @return A person
     */
    @Get("/{uuid}")
    fun getOne(uuid: UUID): Person? {
        logger.debug("Called getOne($uuid)")
        return personService.retrieve(uuid)
    }

    /**
     * Create a person.
     * @param name Name of the person
     * @return A person with their ID
     */
    @Post("/{name}")
    fun postOne(name: String): Person {
        logger.debug("Called postOne($name)")
        val person = Person(null, name)
        return personService.save(person)
    }

    /**
     * Update a person.
     * @param uuid ID of the person
     * @return The updated person
     */
    @Put("/{uuid}")
    fun putOne(uuid: UUID, name: String): Person {
        logger.debug("Called putOne($uuid)")
        val person = Person(null, name)
        return personService.update(uuid, person)
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
            Person(null, "Harry Potter"),
            Person(null, "Hermoine Granger"),
            Person(null, "Ronald Weasley")
        )
    }
}