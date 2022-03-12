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
        val addedPerson = personService.add(person)

        // Act
        val gotPerson = personService.get(addedPerson.id!!)

        // Assert
        assertThat(gotPerson).isEqualTo(addedPerson)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `person exists`(person: Person) {
        // Arrange
        val addedPerson = personService.add(person)

        // Act
        val personExists = personService.exists(addedPerson.id!!)

        // Assert
        assertThat(personExists).isTrue
    }

    @Test
    fun `count persons`() {
        val persons = personProvider().toList()

        val initialPersonCount = personService.count

        persons.forEachIndexed { index, person ->
            // Arrange
            personService.add(person)

            // Act
            val personCount = personService.count

            // Assert
            assertThat(personCount).isEqualTo(initialPersonCount + index + 1)
        }
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

    @Test
    fun `delete person`() {
        // Arrange
        val person = Person(null, "Test")
        val addedPerson = personService.add(person)
        val personCount = personService.count

        // Act
        personService.delete(addedPerson.id!!)
        val personExists = personService.exists(addedPerson.id!!)
        val personCountAfterDeletion = personService.count

        // Assert
        assertThat(personExists).isFalse
        assertThat(personCountAfterDeletion).isEqualTo(personCount - 1)
    }

    @Test
    fun `delete all persons`() {
        val persons = personProvider().toList()

        // Arrange
        for (person in persons) {
            personService.add(person)
        }

        // Act
        personService.deleteAll()
        val personCountAfterDeletion = personService.count

        // Assert
        assertThat(personCountAfterDeletion).isEqualTo(0)
    }

    fun personProvider() = TestDataProvider.personProvider()
}