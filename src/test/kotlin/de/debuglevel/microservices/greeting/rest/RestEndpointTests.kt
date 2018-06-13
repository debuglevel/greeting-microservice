package de.debuglevel.microservices.greeting.rest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import spark.Spark.awaitInitialization
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestEndpointTests {

    init {
        val restEndpoint = RestEndpoint()
        restEndpoint.start(arrayOf())

        awaitInitialization()
    }

    @Test
    fun `server listens on default port`() {
        // Arrange

        // Act
        val response = ApiTestUtils.request("GET", "/greet/test", null)

        // Assert
        // HTTP Codes begin from "100". So something from 100 and above was probably a response to a HTTP request
        assertThat(response?.status).isGreaterThanOrEqualTo(100)
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class `valid requests on greet` {
        @ParameterizedTest
        @MethodSource("validNameProvider")
        fun `server sends greeting in body`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.body).contains(testData.expected)
        }

        @ParameterizedTest
        @MethodSource("validNameProvider")
        fun `server sends status code 200`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.status).isEqualTo(200)
        }

        @ParameterizedTest
        @MethodSource("validNameProvider")
        fun `server sends json content type`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.contentType).isEqualTo("application/json")
        }

        @ParameterizedTest
        @MethodSource("validNameProvider")
        fun `server sends json content`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            val validJson = JsonUtils.isJSONValid(response?.body)
            assertThat(validJson).isTrue()
        }

        fun validNameProvider() = Stream.of(
                NameTestData(value = "Mozart", expected = "Hello, Mozart!"),
                NameTestData(value = "Amadeus", expected = "Hello, Amadeus!"),
                // TODO: Umlauts do not work when executed as gradle task in Windows
//                NameTestData(value = "H%C3%A4nschen", expected = "Hello, Hänschen!"),
                NameTestData(value = "Max%20Mustermann", expected = "Hello, Max Mustermann!")
        )
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class `invalid requests on greet` {
        @ParameterizedTest
        @MethodSource("invalidNameProvider")
        fun `server does not send greeting in body`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.body).doesNotContain("Hello, ${testData.value}!")
        }

        @ParameterizedTest
        @MethodSource("invalidNameProvider")
        fun `server sends error message`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.body).contains("message")
        }

        @ParameterizedTest
        @MethodSource("invalidNameProvider")
        fun `server sends status code 400`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.status).isEqualTo(400)
        }

        @ParameterizedTest
        @MethodSource("invalidNameProvider")
        fun `server sends json content type`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            assertThat(response?.contentType).isEqualTo("application/json")
        }

        @ParameterizedTest
        @MethodSource("invalidNameProvider")
        fun `server sends json content`(testData: NameTestData) {
            // Arrange

            // Act
            val response = ApiTestUtils.request("GET", "/greet/${testData.value}", null)

            // Assert
            val validJson = JsonUtils.isJSONValid(response?.body)
            assertThat(validJson).isTrue()
        }

        fun invalidNameProvider() = Stream.of(
                //NameTestData(value = ""),
                NameTestData(value = "%20")
        )
    }

    data class NameTestData(
            val value: String,
            val expected: String? = null
    )
}