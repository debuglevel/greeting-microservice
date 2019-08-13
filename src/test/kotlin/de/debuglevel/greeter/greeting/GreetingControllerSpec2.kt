package de.debuglevel.greeter.greeting

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GreetingControllerSpec2 {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/greetings")
    lateinit var client: HttpClient

    @Test
    fun `test greeting Mozart`() {
        val response: String = client.toBlocking()
            .retrieve("/Mozart")
        assert(response.contains("Hello, Mozart!"))
    }
}