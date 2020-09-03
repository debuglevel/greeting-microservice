package de.debuglevel.greeter.person

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.server.types.files.StreamedFile
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import java.util.*

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/persons")
@Tag(name = "persons")
class PersonController(private val personService: PersonService) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get all persons
     * @return All persons
     */
    @Get("/")
    fun getAll(): HttpResponse<*> {
        logger.debug("Called getAll()")
        return try {
            val persons = personService.list()
            val getPersonResponses = persons
                .map { GetPersonResponse(it) }

            HttpResponse.ok(getPersonResponses)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: ${e.stackTrace}")
        }
    }

    /**
     * Get a person
     * @param uuid ID of the person
     * @return A person
     */
    @Get("/{uuid}")
    fun getOne(uuid: UUID): HttpResponse<*> {
        logger.debug("Called getOne($uuid)")
        return try {
            val person = personService.get(uuid)

            val getPersonResponse = GetPersonResponse(person)
            HttpResponse.ok(getPersonResponse)
        } catch (e: PersonService.EntityNotFoundException) {
            HttpResponse.notFound("Person $uuid does not exist.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: ${e.stackTrace}")
        }
    }

    /**
     * Download a never ending file of random names
     */
    @Get("/endlessRandom")
    fun downloadRandomEndless(): StreamedFile {
        logger.debug("Called downloadRandomEndless()")

        val inputStream = personService.randomStream()
        return StreamedFile(inputStream, MediaType.TEXT_PLAIN_TYPE)
    }

    /**
     * Create a person.
     * @return A person with their ID
     */
    @Post("/")
    fun postOne(addPersonRequest: AddPersonRequest): HttpResponse<*> {
        logger.debug("Called postOne($addPersonRequest)")

        return try {
            val person = addPersonRequest.toPerson()
            val addedPerson = personService.add(person)

            val addPersonResponse = AddPersonResponse(addedPerson)
            HttpResponse.created(addPersonResponse)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: ${e.stackTrace}")
        }
    }

    /**
     * Update a person.
     * @param uuid ID of the person
     * @return The updated person
     */
    @Put("/{uuid}")
    fun putOne(uuid: UUID, updatePersonRequest: UpdatePersonRequest): HttpResponse<*> {
        logger.debug("Called putOne($uuid, $updatePersonRequest)")

        return try {
            val person = updatePersonRequest.toPerson()
            val updatedPerson = personService.update(uuid, person)

            val updatePersonResponse = UpdatePersonResponse(updatedPerson)
            HttpResponse.ok(updatePersonResponse)
        } catch (e: PersonService.EntityNotFoundException) {
            HttpResponse.notFound("Person $uuid does not exist.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: ${e.stackTrace}")
        }
    }

    /**
     * Get all VIPs
     * @return All VIPs
     */
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/VIPs")
    fun getVIPs(): Set<GetPersonResponse> {
        logger.debug("Called getVIPs()")
        return setOf(
            GetPersonResponse(UUID.randomUUID(), "Harry Potter"),
            GetPersonResponse(UUID.randomUUID(), "Hermoine Granger"),
            GetPersonResponse(UUID.randomUUID(), "Ronald Weasley")
        )
    }
}