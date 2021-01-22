package de.debuglevel.greeter.person

import io.micronaut.core.annotation.Introspected

@Introspected
data class UpdatePersonRequest(
    val name: String,
) {
    fun toPerson(): Person {
        return Person(
            id = null,
            name = this.name,
        )
    }
}