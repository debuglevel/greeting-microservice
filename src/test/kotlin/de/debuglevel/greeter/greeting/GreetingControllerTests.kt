package de.debuglevel.greeter.greeting

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreetingControllerTests {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/greetings")
    lateinit var httpClient: HttpClient

    @ParameterizedTest
    @MethodSource("validNameAndLanguageProvider")
    fun `greet valid names in various languages (GET)`(testData: TestDataProvider.NameTestData) {
        // Arrange

        // Act
        // UriBuilder.expand() handles nasty things like URL encoding of umlauts, spaces, ...
        val uri = UriBuilder.of("/{name}")
            .queryParam("language", testData.language)
            .expand(mutableMapOf("name" to testData.name))
            .toString()
        val greeting = httpClient.toBlocking().retrieve(uri)

        // Assert
        Assertions.assertThat(greeting).contains(testData.expected)
    }

    @ParameterizedTest
    @MethodSource("validNameAndLanguageProvider")
    fun `greet valid names in various languages (POST)`(testData: TestDataProvider.NameTestData) {
        // Arrange
        val greetingRequest = GreetingRequest(testData.name, testData.language)

        // Act
        val uri = UriBuilder.of("/").build()
        val greeting = httpClient.toBlocking()
            .retrieve(HttpRequest.POST(uri, greetingRequest), GreetingDTO::class.java)

        // Assert
        Assertions.assertThat(greeting.greeting).isEqualTo(testData.expected)
    }

    fun validNameAndLanguageProvider() = TestDataProvider.validNameAndLanguageProvider()

    // do not test "invalid" names (i.e. "" and " ") as their HTTP call would translate to "/greetings/" which then returns the getList() stuff
//    @ParameterizedTest
//    @MethodSource("invalidNameProvider")
//    fun `greet invalid names`(testData: TestDataProvider.NameTestData) {
//        // Arrange
//
//        // Act
//        val greeting = httpClient.toBlocking().retrieve("/${testData.name}?language=${testData.language}")
//
//        // Assert
//        Assertions.assertThat(greeting).contains("500")
//    }
//
//    fun invalidNameProvider(): Stream<TestDataProvider.NameTestData> {
//        return TestDataProvider.invalidNameProvider()
//    }
}