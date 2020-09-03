package de.debuglevel.greeter.person

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonClientTests {

    @Inject
    lateinit var personClient: PersonClient

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `add person`(person: Person) {
        // Arrange
        val addPersonRequest = AddPersonRequest(person)

        // Act
        val savedPerson = personClient.postOne(addPersonRequest).blockingGet()

        // Assert
        Assertions.assertThat(savedPerson.name).isEqualTo(person.name)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `get person`(person: Person) {
        // Arrange
        val addPersonRequest = AddPersonRequest(person)
        val addedPerson = personClient.postOne(addPersonRequest).blockingGet()

        // Act
        val getPerson = personClient.getOne(addedPerson.id).blockingGet()

        // Assert
        Assertions.assertThat(getPerson.id).isEqualTo(addedPerson.id)
        Assertions.assertThat(getPerson.name).isEqualTo(person.name)
        Assertions.assertThat(getPerson.name).isEqualTo(addedPerson.name)
    }

    @Test
    fun `get VIPs`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:SECRET_PASSWORD".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val retrievedPersons = personClient.getVIPs(basicAuthenticationHeader)

        // Assert
        Assertions.assertThat(retrievedPersons).anyMatch { it.name == "Hermoine Granger" }
        Assertions.assertThat(retrievedPersons).anyMatch { it.name == "Harry Potter" }
        Assertions.assertThat(retrievedPersons).anyMatch { it.name == "Ronald Weasley" }
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