package de.debuglevel.greeter.person

import java.util.*

data class GetPersonResponse(
    var id: UUID,
    var name: String,
) {
    constructor(person: Person) : this(
        person.id!!,
        person.name,
    )
}