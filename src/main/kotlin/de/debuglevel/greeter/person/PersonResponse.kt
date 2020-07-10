package de.debuglevel.greeter.person

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class PersonResponse(
    var id: UUID?,
    var name: String
) {
    constructor(person: Person) : this(
        person.id,
        person.name
    )
}