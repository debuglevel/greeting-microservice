package de.debuglevel.greeter.person

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class UpdatePersonResponse(
    val id: UUID,
    val name: String,
) {
    constructor(person: Person) : this(
        person.id!!,
        person.name,
    )
}