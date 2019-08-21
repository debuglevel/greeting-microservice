package de.debuglevel.greeter.person

import java.util.stream.Stream

object TestDataProvider {
    fun personProvider() = Stream.of(
        Person(
            id = 0,
            name = "Mozart"
        ),
        Person(
            id = 0,
            name = "Hänschen"
        ),
        Person(
            id = 0,
            name = "コハウプト マルク"
        ),
        Person(
            id = 0,
            name = "Max Mustermann"
        )
    )
}