package de.debuglevel.greeter.person

import io.micronaut.http.HttpResponse
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
     * Get all persons
     * @return All persons
     */
    @Get("/")
    fun getAll(): HttpResponse<Set<PersonResponse>> {
        logger.debug("Called getAll()")
        return try {
            val persons = personService.list()
            val personsResponse = persons
                .map { PersonResponse(it) }
                .toSet()

            HttpResponse.ok(personsResponse)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError<Set<PersonResponse>>()
        }
    }

    /**
     * Get a person
     * @param uuid ID of the person
     * @return A person
     */
    @Get("/{uuid}")
    fun getOne(uuid: UUID): HttpResponse<PersonResponse> {
        logger.debug("Called getOne($uuid)")
        return try {
            val person = personService.get(uuid)
            HttpResponse.ok(PersonResponse(person))
        } catch (e: PersonService.EntityNotFoundException) {
            HttpResponse.badRequest<PersonResponse>()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError<PersonResponse>()
        }
    }

    /**
     * Create a person.
     * @return A person with their ID
     */
    @Post("/")
    fun postOne(personRequest: PersonRequest): HttpResponse<PersonResponse> {
        logger.debug("Called postOne($personRequest)")

        return try {
            val person = Person(
                id = null,
                name = personRequest.name
            )
            val addedPerson = personService.add(person)

            HttpResponse.created(PersonResponse(addedPerson))
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError<PersonResponse>()
        }
    }

    /**
     * Update a person.
     * @param uuid ID of the person
     * @return The updated person
     */
    @Put("/{uuid}")
    fun putOne(uuid: UUID, personRequest: PersonRequest): HttpResponse<PersonResponse> {
        logger.debug("Called putOne($uuid, $personRequest)")

        return try {
            val person = Person(
                id = null,
                name = personRequest.name
            )
            val updatedPerson = personService.update(uuid, person)

            HttpResponse.ok(PersonResponse(updatedPerson))
        } catch (e: PersonService.EntityNotFoundException) {
            HttpResponse.badRequest<PersonResponse>()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError<PersonResponse>()
        }
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