package de.debuglevel.greeter.person

import java.util.stream.Stream

object TestDataProvider {
    fun personProvider() = Stream.of(
        Person(
            id = null,
            name = "Mozart"
        ),
        Person(
            id = null,
            name = "Hänschen"
        ),
        Person(
            id = null,
            name = "コハウプト マルク"
        ),
        Person(
            id = null,
            name = "Max Mustermann"
        )
    )
}