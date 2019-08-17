package de.debuglevel.greeter

import de.debuglevel.greeter.Application.main
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.URL

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTests {
    @Test
    fun `standalone startup`() {
        // Arrange
        main(arrayOf())
        val port = MicronautUtils.getPort(Application.applicationContext)

        // Act
        val status =
            try {
                HttpClient
                    .create(URL("http://localhost:$port/"))
                    .toBlocking()
                    .exchange<String>("/")
                    .status
            } catch (ex: HttpClientResponseException) {
                ex.response.status
            }


        // Assert
        // HTTP Codes begin from "100". So something from 100 and above was probably a response to a HTTP request
        Assertions.assertThat(status.code).isGreaterThanOrEqualTo(100)
    }
}