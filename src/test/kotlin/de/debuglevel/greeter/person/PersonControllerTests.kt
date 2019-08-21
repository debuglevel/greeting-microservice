package de.debuglevel.greeter.person

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTests {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/persons")
    lateinit var httpClient: HttpClient

    @Test
    fun `save person`() {
        // Arrange
        val person = Person(0, "Mozart")

        // Act
        val uri = UriBuilder.of("/{name}")
            .expand(mutableMapOf("name" to person.name))
            .toString()
        val savedPerson = httpClient.toBlocking()
            .retrieve(HttpRequest.POST(uri, ""), PersonDTO::class.java)

        // Assert
        Assertions.assertThat(savedPerson.name).isEqualTo(person.name)
    }

    @Test
    fun `retrieve person`() {
        // Arrange
        val person = Person(0, "Mozart")
        val saveUri = UriBuilder.of("/{name}")
            .expand(mutableMapOf("name" to person.name))
            .toString()
        val savedPerson = httpClient.toBlocking()
            .retrieve(HttpRequest.POST(saveUri, ""), PersonDTO::class.java)

        // Act
        val retrieveUri = UriBuilder.of("/{id}")
            .expand(mutableMapOf("id" to savedPerson.id))
            .toString()
        val retrievedPerson = httpClient.toBlocking()
            .retrieve(retrieveUri, PersonDTO::class.java)

        // Assert
        Assertions.assertThat(retrievedPerson.id).isEqualTo(savedPerson.id)
        Assertions.assertThat(retrievedPerson.name).isEqualTo(savedPerson.name)
    }
}