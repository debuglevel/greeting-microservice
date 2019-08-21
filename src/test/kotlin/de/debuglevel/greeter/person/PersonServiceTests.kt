package de.debuglevel.greeter.person

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonServiceTests {

    @Inject
    lateinit var personService: PersonService

    @Test
    fun `save person`() {
        // Arrange
        val person = Person(0, "Mozart")

        // Act
        val savedPerson = personService.save(person)

        // Assert
        assertThat(savedPerson).isEqualTo(person)
    }

    @Test
    fun `retrieve person`() {
        // Arrange
        val person = Person(0, "Mozart")
        val savedPerson = personService.save(person)

        // Act
        val retrievedPerson = personService.retrieve(savedPerson.id)

        // Assert
        assertThat(retrievedPerson).isEqualTo(savedPerson)
    }
}