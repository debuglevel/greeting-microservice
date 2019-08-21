package de.debuglevel.greeter.person

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
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
        val savedPerson = personService.save(person)

        // Assert
        assertThat(savedPerson).isEqualTo(person)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `retrieve person`(person: Person) {
        // Arrange
        val savedPerson = personService.save(person)

        // Act
        val retrievedPerson = personService.retrieve(savedPerson.id)

        // Assert
        assertThat(retrievedPerson).isEqualTo(savedPerson)
    }

    fun personProvider() = TestDataProvider.personProvider()
}