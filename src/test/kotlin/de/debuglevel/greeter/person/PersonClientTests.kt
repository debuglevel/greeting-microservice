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
    @MethodSource("personDtoProvider")
    fun `save person`(person: PersonDTO) {
        // Arrange

        // Act
        val savedPerson = personClient.postOne(person.name).blockingGet()

        // Assert
        Assertions.assertThat(savedPerson.name).isEqualTo(person.name)
    }

    @ParameterizedTest
    @MethodSource("personDtoProvider")
    fun `retrieve person`(person: PersonDTO) {
        // Arrange
        val savedPerson = personClient.postOne(person.name).blockingGet()

        // Act
        val retrievedPerson = personClient.getOne(savedPerson.id).blockingGet()

        // Assert
        Assertions.assertThat(retrievedPerson.id).isEqualTo(savedPerson.id)
        Assertions.assertThat(retrievedPerson.name).isEqualTo(savedPerson.name)
    }

    @Test
    fun `retrieve VIPs`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("myUser:secretPassword".byteInputStream().readBytes())
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
            Base64.getEncoder().encodeToString("myUser:wrongPassword".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val thrown = Assertions.catchThrowable {
            personClient.getVIPs(basicAuthenticationHeader)
        }

        // Assert
        Assertions.assertThat(thrown)
            .isInstanceOf(HttpClientResponseException::class.java)
            .hasMessageContaining("Unauthorized")
    }

    fun personDtoProvider() = TestDataProvider.personProvider()
        .map {
            PersonDTO(it.id, it.name)
        }
}