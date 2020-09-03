package de.debuglevel.greeter.person

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject


@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTests {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/persons")
    lateinit var httpClient: HttpClient

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `add person`(person: Person) {
        // Arrange
        val addPersonRequest = AddPersonRequest(person)

        // Act
        val addUri = UriBuilder.of("/").build()
        val addedPerson = httpClient.toBlocking()
            .retrieve(HttpRequest.POST(addUri, addPersonRequest), AddPersonResponse::class.java)

        // Assert
        Assertions.assertThat(addedPerson.name).isEqualTo(person.name)
        Assertions.assertThat(addedPerson.name).isEqualTo(addPersonRequest.name)
    }

    @ParameterizedTest
    @MethodSource("personProvider")
    fun `get person`(person: Person) {
        // Arrange
        val addPersonRequest = AddPersonRequest(person)
        val addUri = UriBuilder.of("/").build()
        val addedPerson = httpClient.toBlocking()
            .retrieve(HttpRequest.POST(addUri, addPersonRequest), AddPersonResponse::class.java)

        // Act
        val getUri = UriBuilder.of("/{id}")
            .expand(mutableMapOf("id" to addedPerson.id))
            .toString()
        val getPerson = httpClient.toBlocking()
            .retrieve(getUri, GetPersonResponse::class.java)

        // Assert
        Assertions.assertThat(getPerson.id).isEqualTo(addedPerson.id)
        Assertions.assertThat(getPerson.name).isEqualTo(person.name)
        Assertions.assertThat(getPerson.name).isEqualTo(addedPerson.name)
    }

    @Test
    fun `get VIPs`() {
        // Arrange

        // Act
        val getVipUri = UriBuilder.of("/VIPs").build()
        val httpRequest = HttpRequest
            .GET<String>(getVipUri)
            .basicAuth("SECRET_USERNAME", "SECRET_PASSWORD")
        val argument = Argument.of(List::class.java, GetPersonResponse::class.java)
        val getPersons = httpClient.toBlocking()
            .retrieve(httpRequest, argument) as List<GetPersonResponse>

        // Assert
        Assertions.assertThat(getPersons).anyMatch { it.name == "Hermoine Granger" }
        Assertions.assertThat(getPersons).anyMatch { it.name == "Harry Potter" }
        Assertions.assertThat(getPersons).anyMatch { it.name == "Ronald Weasley" }
    }

    @Test
    fun `fail retrieving VIPs without authentication`() {
        // Arrange

        // Act
        val getVipUri = UriBuilder.of("/VIPs").build()
        val httpRequest = HttpRequest
            .GET<String>(getVipUri)
        val thrown = catchThrowable {
            httpClient.toBlocking().retrieve(httpRequest)
        }

        // Assert
        Assertions.assertThat(thrown)
            .isInstanceOf(HttpClientResponseException::class.java)
            .hasMessageContaining("Unauthorized")
    }

    fun personProvider() = TestDataProvider.personProvider()
}