package de.debuglevel.greeter.greeting

import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GreetingClientSpec {

    @Inject
    lateinit var client: GreetingClient

    @Test
    fun `test greeting Mozart with GreetingClient`() {
        // TODO: no idea why this works. the client seems to pass "Mozart" to server, but I have no clue why.
        assert(client.getOne().blockingGet().contains("Hello, Mozart!"))
    }
}