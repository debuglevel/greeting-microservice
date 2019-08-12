package de.debuglevel.greeting.domain.greeting

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreetingServiceTests {
    @ParameterizedTest
    @MethodSource("validNameProvider")
    fun `greet valid names`(testData: NameTestData) {
        // Arrange

        // Act
        val greeting = GreetingService.greet(testData.value).greeting

        //Assert
        assertThat(greeting).isEqualTo(testData.expected)
    }

    fun validNameProvider() = Stream.of(
        NameTestData(value = "Mozart", expected = "Hello, Mozart!"),
        NameTestData(value = "Amadeus", expected = "Hello, Amadeus!"),
        NameTestData(value = "Hänschen", expected = "Hello, Hänschen!"),
        NameTestData(value = "Max Mustermann", expected = "Hello, Max Mustermann!")
    )

    @ParameterizedTest
    @MethodSource("invalidNameProvider")
    fun `format invalid names`(testData: NameTestData) {
        // Arrange

        // Act

        // Assert
        assertThatExceptionOfType(GreetingService.GreetingException::class.java).isThrownBy({
            GreetingService.greet(
                testData.value
            )
        })
    }

    fun invalidNameProvider() = Stream.of(
        NameTestData(value = ""),
        NameTestData(value = " ")
    )

    data class NameTestData(
        val value: String,
        val expected: String? = null
    )
}