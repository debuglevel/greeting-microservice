package de.debuglevel.greeter.greeting

import java.util.stream.Stream

object TestDataProvider {
    data class NameTestData(
        val name: String,
        val language: String?,
        val expected: String? = null
    )

    fun validNameAndLanguageProvider() = Stream.of(
        NameTestData(
            name = "Mozart",
            language = "de_DE",
            expected = "Grüß Gott, Mozart"
        ),
        NameTestData(
            name = "Amadeus",
            language = "de_CH",
            expected = "Grüezi Amadeus!"
        ),
        NameTestData(
            name = "Hänschen",
            language = "en_US",
            expected = "Howdy, Hänschen!"
        ),
        NameTestData(
            name = "コハウプト マルク",
            language = "en_US",
            expected = "Howdy, コハウプト マルク!"
        ),
        NameTestData(
            name = "Max Mustermann",
            language = "en_GB",
            expected = "Good morning, Max Mustermann."
        ),
        NameTestData(
            name = "Max Mustermann",
            language = "en_GB",
            expected = "Good morning, Max Mustermann."
        ),
        NameTestData(
            name = "Max Mustermann",
            language = null,
            expected = "You did not provide a language, but I'll try english: Hello, Max Mustermann!"
        ),
        NameTestData(
            name = "Max Mustermann",
            language = "",
            expected = "You did not provide a language, but I'll try english: Hello, Max Mustermann!"
        ),
        NameTestData(
            name = "Max Mustermann",
            language = "zz_ZZ",
            expected = "I don't speak your language, but I still want to say hello, Max Mustermann"
        )
    )

    fun invalidNameProvider() = Stream.of(
        NameTestData(name = "", language = "de_DE"),
        NameTestData(name = " ", language = "de_DE")
    )
}