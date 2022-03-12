package de.debuglevel.greeter.person

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTests {
    @Inject
    lateinit var personClient: PersonClient

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `add person`(person: Person) {
        // Arrange
        val addPersonRequest = AddPersonRequest(person)

        // Act
        val addedPerson = personClient.add(addPersonRequest).block()

        // Assert
        Assertions.assertThat(addedPerson.name).isEqualTo(person.name)
        Assertions.assertThat(addedPerson.name).isEqualTo(addPersonRequest.name)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `get person`(person: Person) {
        // Arrange
        val addPersonRequest = AddPersonRequest(person)
        val addedPerson = personClient.add(addPersonRequest).block()

        // Act
        val getPerson = personClient.get(addedPerson.id).block()

        // Assert
        Assertions.assertThat(getPerson.id).isEqualTo(addedPerson.id)
        Assertions.assertThat(getPerson.name).isEqualTo(person.name)
        Assertions.assertThat(getPerson.name).isEqualTo(addedPerson.name)
    }

    @Test
    fun `get non-existing person`() {
        // Arrange

        // Act
        val getPersonResponse = personClient.get(UUID.randomUUID()).block()

        // Assert
        Assertions.assertThat(getPersonResponse).isNull()
    }

    @Test
    fun `update person`() {
        // Arrange
        val addPersonRequest = AddPersonRequest("Original Name")
        val addedPerson = personClient.add(addPersonRequest).block()
        val updatePersonRequest = UpdatePersonRequest("Updated Name")

        // Act
        val updatedPerson = personClient.update(addedPerson.id, updatePersonRequest).block()
        val getPerson = personClient.get(addedPerson.id).block()

        // Assert
        Assertions.assertThat(updatedPerson.id).isEqualTo(addedPerson.id)
        Assertions.assertThat(getPerson.id).isEqualTo(addedPerson.id)
        Assertions.assertThat(updatedPerson.name).isEqualTo(updatePersonRequest.name)
    }

    @Test
    fun `update non-existing person`() {
        // Arrange
        val updatePersonRequest = UpdatePersonRequest("Updated Name")

        // Act
        val getPersonResponse = personClient.update(UUID.randomUUID(), updatePersonRequest).block()

        // Assert
        Assertions.assertThat(getPersonResponse).isNull()
    }

    @Test
    fun `get all persons`() {
        // Arrange
        personProvider().forEach {
            personClient.add(AddPersonRequest(it)).block()
        }

        // Act
        val getPersons = personClient.getAll()

        // Assert
        Assertions.assertThat(getPersons).extracting<String> { x -> x.name }
            .containsAll(personProvider().map { it.name }.toList())
    }

    @Test
    fun `get VIPs`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:SECRET_PASSWORD".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val getPersons = personClient.getVIPs(basicAuthenticationHeader)

        // Assert
        Assertions.assertThat(getPersons).anyMatch { it.name == "Hermione Granger" }
        Assertions.assertThat(getPersons).anyMatch { it.name == "Harry Potter" }
        Assertions.assertThat(getPersons).anyMatch { it.name == "Ronald Weasley" }
    }

    @Test
    fun `fail get VIPs with bad authentication`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:wrongPassword".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val thrown = Assertions.catchThrowable {
            personClient.getVIPs(basicAuthenticationHeader)
        }

        // Assert
        Assertions.assertThat(thrown)
            .isInstanceOf(HttpClientResponseException::class.java)
            .hasMessageContaining("Unauthorized")
    }

    fun personProvider() = TestDataProvider.personProvider()
}