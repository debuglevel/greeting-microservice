package de.debuglevel.greeter.person

import mu.KotlinLogging
import java.util.*
import javax.inject.Singleton

@Singleton
class PersonService(
    private val personRepository: PersonRepository
) {
    private val logger = KotlinLogging.logger {}

    fun retrieve(id: UUID): Person? {
        logger.debug { "Getting person with ID '$id'..." }

        val person: Person? = personRepository.findById(id).orElse(null)

        logger.debug { "Got person with ID '$id': $person" }
        return person
    }

    fun save(person: Person): Person {
        logger.debug { "Saving person '$person'..." }

        val savedPerson = personRepository.save(person)

        logger.debug { "Saved person: $savedPerson" }
        return person
    }

    fun update(id: UUID, person: Person): Person {
        logger.debug { "Saving person '$person' with ID '$id'..." }

        // TODO: that's not nice; the whole service should be rewritten to throw an exception instead of returning null if no entity was found.
        // an object must be known to Hibernate (i.e. retrieved first) to get updated; it would be a "detached entity" otherwise.
        val updatePerson = this.retrieve(id)?.apply {
            name = person.name
        }!!

        val savedPerson = personRepository.save(updatePerson)

        logger.debug { "Saved person: $savedPerson with ID '$id'" }
        return person
    }
}