package de.debuglevel.greeter.greeting

import io.micronaut.context.ApplicationContext
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object GreetingControllerSpec : Spek({
    describe("GreetingController Suite") {
        val embeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client = HttpClient.create(embeddedServer.url)

        it("test /greetings/Mozart responds 'Hello, Mozart!'") {
            val response: String = client.toBlocking().retrieve("/greetings/Mozart")
            assert(response.contains("Hello, Mozart!"))
        }

        afterGroup {
            client.close()
            embeddedServer.close()
        }
    }
})