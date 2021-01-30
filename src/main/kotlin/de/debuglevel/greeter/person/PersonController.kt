package de.debuglevel.greeter.person

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
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
    fun getAllPersons(): HttpResponse<List<GetPersonResponse>> {
        logger.debug("Called getAllPersons()")
        return try {
            val persons = personService.list()
            val getPersonResponses = persons
                .map { GetPersonResponse(it) }

            HttpResponse.ok(getPersonResponses)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Get a person
     * @param id ID of the person
     * @return A person
     */
    @Get("/{id}")
    fun getOnePerson(id: UUID): HttpResponse<GetPersonResponse> {
        logger.debug("Called getOnePerson($id)")
        return try {
            val person = personService.get(id)

            val getPersonResponse = GetPersonResponse(person)
            HttpResponse.ok(getPersonResponse)
        } catch (e: PersonService.EntityNotFoundException) {
            logger.debug { "Getting person $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
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
    fun postOnePerson(addPersonRequest: AddPersonRequest): HttpResponse<AddPersonResponse> {
        logger.debug("Called postOnePerson($addPersonRequest)")

        return try {
            val person = addPersonRequest.toPerson()
            val addedPerson = personService.add(person)

            val addPersonResponse = AddPersonResponse(addedPerson)
            HttpResponse.created(addPersonResponse)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Update a person.
     * @param id ID of the person
     * @return The updated person
     */
    @Put("/{id}")
    fun putOnePerson(id: UUID, updatePersonRequest: UpdatePersonRequest): HttpResponse<UpdatePersonResponse> {
        logger.debug("Called putOnePerson($id, $updatePersonRequest)")

        return try {
            val person = updatePersonRequest.toPerson()
            val updatedPerson = personService.update(id, person)

            val updatePersonResponse = UpdatePersonResponse(updatedPerson)
            HttpResponse.ok(updatePersonResponse)
        } catch (e: PersonService.EntityNotFoundException) {
            logger.debug { "Updating person $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete a person.
     * @param id ID of the person
     */
    @Delete("/{id}")
    fun deleteOnePerson(id: UUID): HttpResponse<Unit> {
        logger.debug("Called deleteOnePerson($id)")
        return try {
            personService.delete(id)

            HttpResponse.noContent()
        } catch (e: PersonService.EntityNotFoundException) {
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete all person.
     */
    @Delete("/")
    fun deleteAllPersons(): HttpResponse<Unit> {
        logger.debug("Called deleteAllPersons()")
        return try {
            personService.deleteAll()

            HttpResponse.noContent()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
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
            GetPersonResponse(UUID.randomUUID(), "Hermione Granger"),
            GetPersonResponse(UUID.randomUUID(), "Ronald Weasley")
        )
    }
}