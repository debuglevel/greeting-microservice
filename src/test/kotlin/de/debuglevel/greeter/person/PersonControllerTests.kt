package de.debuglevel.greeter.person

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
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

//    @Test
//    fun `save person`() {
//        // Arrange
//        val uri = UriBuilder.of("/{name}")
//            .expand(mutableMapOf("name" to person.name))
//            .toString()
//
//        // Act
//        // TODO: ... POST
//        val savedPerson = httpClient.toBlocking().retrieve(uri)
//
//        // Assert
//        Assertions.assertThat(savedPerson).contains(person.name)
//    }
//
//    @Test
//    fun `retrieve person`() {
//        // Arrange
//        // TODO: ... POST person
//        val uri = UriBuilder.of("/{id}")
//            .expand(mutableMapOf("name" to savedPerson.id))
//            .toString()
//
//        // Act
//        val retrievedPerson = httpClient.toBlocking().retrieve(uri)
//
//        // Assert
//        Assertions.assertThat(retrievedPerson).contains(person.id)
//        Assertions.assertThat(retrievedPerson).contains(person.name)
//    }
}