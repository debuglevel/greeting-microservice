package de.debuglevel.microservices.greeting

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreeterTests {
    @ParameterizedTest
    @MethodSource("validNameProvider")
    fun `greet valid names`(testData: NameTestData) {
        assertThat(Greeter.greet(testData.value).greeting).isEqualTo(testData.expected)
    }

    fun validNameProvider() = Stream.of(
            NameTestData(value = "Mozart", expected = "Hello, Mozart!"),
            NameTestData(value = "Amadeus", expected = "Hello, Amadeus!")
    )

    @ParameterizedTest
    @MethodSource("invalidNameProvider")
    fun `format invalid names`(testData: NameTestData) {
        assertThatExceptionOfType(Greeter.GreetingException::class.java).isThrownBy({ Greeter.greet(testData.value) })
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