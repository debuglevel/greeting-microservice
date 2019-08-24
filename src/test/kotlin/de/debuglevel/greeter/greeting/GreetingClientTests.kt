package de.debuglevel.greeter.greeting

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreetingClientTests {

    @Inject
    lateinit var greetingClient: GreetingClient

    @ParameterizedTest
    @MethodSource("validNameAndLanguageProvider")
    fun `greet valid names in various languages (GET)`(testData: TestDataProvider.NameTestData) {
        // Arrange

        // Act
        val greeting = greetingClient.getOne(testData.name, testData.language).blockingGet()

        // Assert
        Assertions.assertThat(greeting.greeting).isEqualTo(testData.expected)
    }

    @ParameterizedTest
    @MethodSource("validNameAndLanguageProvider")
    fun `greet valid names in various languages (POST)`(testData: TestDataProvider.NameTestData) {
        // Arrange
        val greetingRequest = GreetingRequest(testData.name, testData.language)

        // Act
        val greeting = greetingClient.postOne(greetingRequest).blockingGet()

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
//        val x = greetingClient.getOne(
//            testData.name,
//            testData.language
//        ).blockingGet()
//
//        // Assert
//        Assertions.assertThatExceptionOfType(GreetingService.GreetingException::class.java).isThrownBy {
//
//        }
//    }
//
//    fun invalidNameProvider() = TestDataProvider.invalidNameProvider()
}