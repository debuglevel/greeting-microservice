package de.debuglevel.greeter.person

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import mu.KotlinLogging

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
}