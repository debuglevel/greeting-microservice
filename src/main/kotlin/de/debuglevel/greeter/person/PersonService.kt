package de.debuglevel.greeter.person

import mu.KotlinLogging
import java.util.*
import javax.inject.Singleton

@Singleton
class PersonService(
    private val personRepository: PersonRepository
) {
    private val logger = KotlinLogging.logger {}

    fun get(id: UUID): Person {
        logger.debug { "Getting person with ID '$id'..." }

        val person: Person = personRepository.findById(id).orElseThrow { EntityNotFoundException(id) }

        logger.debug { "Got person with ID '$id': $person" }
        return person
    }

    fun add(person: Person): Person {
        logger.debug { "Saving person '$person'..." }

        val savedPerson = personRepository.save(person)

        logger.debug { "Saved person: $savedPerson" }
        return person
    }

    fun update(id: UUID, person: Person): Person {
        logger.debug { "Saving person '$person' with ID '$id'..." }

        // an object must be known to Hibernate (i.e. retrieved first) to get updated;
        // it would be a "detached entity" otherwise.
        val updatePerson = this.get(id).apply {
            name = person.name
        }

        val savedPerson = personRepository.save(updatePerson)

        logger.debug { "Saved person: $savedPerson with ID '$id'" }
        return person
    }

    fun list(): Set<Person> {
        logger.debug { "Getting all persons ..." }

        val persons = personRepository.findAll().toSet()

        logger.debug { "Got all persons" }
        return persons
    }

    class EntityNotFoundException(criteria: Any) : Exception("Entity '$criteria' does not exist.")
}