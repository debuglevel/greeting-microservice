package de.debuglevel.greeter.greeting

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreetingServiceTests {

    @Inject
    lateinit var greetingService: GreetingService

    @ParameterizedTest
    @MethodSource("validNameAndLanguageProvider")
    fun `greet valid names in various languages`(testData: TestDataProvider.NameTestData) {
        // Arrange

        // Act
        val greeting = greetingService.greet(testData.name, testData.language)

        // Assert
        assertThat(greeting.greeting).isEqualTo(testData.expected)
    }

    fun validNameAndLanguageProvider() = TestDataProvider.validNameAndLanguageProvider()

    @ParameterizedTest
    @MethodSource("invalidNameProvider")
    fun `greet invalid names`(testData: TestDataProvider.NameTestData) {
        // Arrange

        // Act

        // Assert
        assertThatExceptionOfType(GreetingService.GreetingException::class.java).isThrownBy {
            greetingService.greet(
                testData.name,
                testData.language
            )
        }
    }

    fun invalidNameProvider() = TestDataProvider.invalidNameProvider()
}