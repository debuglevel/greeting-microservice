package de.debuglevel.greeter.person

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonServiceTests {

    @Inject
    lateinit var personService: PersonService

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `add person`(person: Person) {
        // Arrange

        // Act
        val addedPerson = personService.add(person)

        // Assert
        assertThat(addedPerson).isEqualTo(person)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `get person`(person: Person) {
        // Arrange
        val savedPerson = personService.add(person)

        // Act
        val gotPerson = personService.get(savedPerson.id!!)

        // Assert
        assertThat(gotPerson).isEqualTo(savedPerson)
    }

    @Test
    fun `update person`() {
        // Arrange
        val person = Person(null, "Test")
        val addedPerson = personService.add(person)

        // Act
        val gotPerson = personService.get(addedPerson.id!!)
        gotPerson.name = "Test updated"
        val updatedPerson = personService.update(gotPerson.id!!, gotPerson)

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
        val addedPerson = personService.add(person)

        // Act
        val gotPerson = personService.get(addedPerson.id!!)
        val updatePerson = gotPerson.copy(name = "Test updated")
        val updatedPerson = personService.update(updatePerson.id!!, updatePerson)

        // Assert
        assertThat(updatedPerson.name).isEqualTo("Test updated")
    }

    fun personProvider() = TestDataProvider.personProvider()
}