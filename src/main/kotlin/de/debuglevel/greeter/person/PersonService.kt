package de.debuglevel.greeter.person

import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class PersonService(
    private val personRepository: PersonRepository
) {
    private val logger = KotlinLogging.logger {}

    fun retrieve(id: Long): Person? {
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
}