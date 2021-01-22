package de.debuglevel.greeter.person

import mu.KotlinLogging
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.*
import javax.inject.Singleton
import kotlin.concurrent.thread

@Singleton
class PersonService(
    private val personRepository: PersonRepository,
    private val personGeneratorService: PersonGeneratorService,
) {
    private val logger = KotlinLogging.logger {}

    fun get(id: UUID): Person {
        logger.debug { "Getting person with ID '$id'..." }

        val person: Person = personRepository.findById(id).orElseThrow {
            logger.debug { "Getting person with ID '$id' failed" }
            EntityNotFoundException(id)
        }

        logger.debug { "Got person with ID '$id': $person" }
        return person
    }

    fun add(person: Person): Person {
        logger.debug { "Adding person '$person'..." }

        val savedPerson = personRepository.save(person)

        logger.debug { "Added person: $savedPerson" }
        return savedPerson
    }

    fun update(id: UUID, person: Person): Person {
        logger.debug { "Updating person '$person' with ID '$id'..." }

        // an object must be known to Hibernate (i.e. retrieved first) to get updated;
        // it would be a "detached entity" otherwise.
        val updatePerson = this.get(id).apply {
            name = person.name
        }

        val updatedPerson = personRepository.update(updatePerson)

        logger.debug { "Updated person: $updatedPerson with ID '$id'" }
        return updatedPerson
    }

    fun list(): Set<Person> {
        logger.debug { "Getting all persons ..." }

        val persons = personRepository.findAll().toSet()

        logger.debug { "Got all persons" }
        return persons
    }

    fun delete(id: UUID) {
        logger.debug { "Deleting person with ID '$id'..." }

        if (personRepository.existsById(id)) {
            personRepository.deleteById(id)
        } else {
            throw EntityNotFoundException(id)
        }

        logger.debug { "Deleted person with ID '$id'" }
    }

    fun deleteAll() {
        logger.debug { "Deleting all persons..." }

        val countBefore = personRepository.count()
        personRepository.deleteAll() // CAVEAT: does not delete dependent entities; use this instead: personRepository.findAll().forEach { personRepository.delete(it) }
        val countAfter = personRepository.count()
        val countDeleted = countBefore - countAfter

        logger.debug { "Deleted $countDeleted of $countBefore persons, $countAfter remaining" }
    }

    fun randomStream(): InputStream {
        val outputStream = PipedOutputStream()

        val inputStream = PipedInputStream()
        inputStream.connect(outputStream)

        thread(start = true) {
            logger.debug { "Thread ${Thread.currentThread()} started." }

            var pipeOpen = true
            while (pipeOpen) {
                logger.debug { "Thread ${Thread.currentThread()} sleeping..." }
                Thread.sleep(500)

                val name = personGeneratorService.generateRandom().name
                val byteArray = "$name\n".toByteArray()

                try {
                    logger.debug { "Writing '$name' to OutputStream..." }
                    outputStream.write(byteArray)
                    logger.debug { "Wrote '$name' to OutputStream." }
                } catch (e: Exception) {
                    logger.warn(e) { "Writing to OutputStream failed" }
                    pipeOpen = false
                }
            }

            logger.debug { "Pipe was closed; Thread is ending..." }
        }

        return inputStream
    }

    class EntityNotFoundException(criteria: Any) : Exception("Entity '$criteria' does not exist.")
}