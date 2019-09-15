package de.debuglevel.greeter.person

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonServiceTests {

    @Inject
    lateinit var personService: PersonService

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `save person`(person: Person) {
        // Arrange

        // Act
        val savedPerson = personService.add(person)

        // Assert
        assertThat(savedPerson).isEqualTo(person)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `retrieve person`(person: Person) {
        // Arrange
        val savedPerson = personService.add(person)

        // Act
        val retrievedPerson = personService.get(savedPerson.id!!)

        // Assert
        assertThat(retrievedPerson).isEqualTo(savedPerson)
    }

    @Test
    fun `update person`() {
        // Arrange
        val person = Person(null, "Test")
        val savedPerson = personService.add(person)

        // Act
        val retrievedPerson = personService.get(savedPerson.id!!)
        retrievedPerson.name = "Test updated"
        val updatedPerson = personService.update(retrievedPerson.id!!, retrievedPerson)

        // Assert
        assertThat(updatedPerson.name).isEqualTo("Test updated")
    }

    /**
     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
     */
    @Test
    fun `update person with copy()`() {
        // Arrange
        val person = Person(null, "Test")
        val savedPerson = personService.add(person)

        // Act
        val retrievedPerson = personService.get(savedPerson.id!!)
        val updatePerson = retrievedPerson.copy(name = "Test updated")
        val updatedPerson = personService.update(updatePerson.id!!, updatePerson)

        // Assert
        assertThat(updatedPerson.name).isEqualTo("Test updated")
    }

    fun personProvider() = TestDataProvider.personProvider()
}