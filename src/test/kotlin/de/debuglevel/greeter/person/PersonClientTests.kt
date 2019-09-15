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
    @MethodSource("personRequestProvider")
    fun `save person`(personRequest: PersonRequest) {
        // Arrange

        // Act
        val savedPerson = personClient.postOne(personRequest).blockingGet()

        // Assert
        Assertions.assertThat(savedPerson.name).isEqualTo(personRequest.name)
    }

    @ParameterizedTest
    @MethodSource("personRequestProvider")
    fun `retrieve person`(personRequest: PersonRequest) {
        // Arrange
        val savedPerson = personClient.postOne(personRequest).blockingGet()

        // Act
        val retrievedPerson = personClient.getOne(savedPerson.id!!).blockingGet()

        // Assert
        Assertions.assertThat(retrievedPerson.id).isEqualTo(savedPerson.id)
        Assertions.assertThat(retrievedPerson.name).isEqualTo(savedPerson.name)
        Assertions.assertThat(retrievedPerson).isEqualTo(savedPerson)
    }

    @Test
    fun `retrieve VIPs`() {
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
    fun `fail retrieving VIPs with bad authentication`() {
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

    fun personRequestProvider() = TestDataProvider.personProvider()
        .map {
            PersonRequest(it.id, it.name)
        }
}