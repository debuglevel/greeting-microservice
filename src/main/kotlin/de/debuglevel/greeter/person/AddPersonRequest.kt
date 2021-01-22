package de.debuglevel.greeter.person

import io.micronaut.core.annotation.Introspected

@Introspected
data class AddPersonRequest(
    val name: String,
) {
    constructor(person: Person) : this(
        name = person.name
    )

    fun toPerson(): Person {
        return Person(
            id = null,
            name = this.name,
        )
    }
}