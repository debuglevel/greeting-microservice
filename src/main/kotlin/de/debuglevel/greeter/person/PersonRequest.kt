package de.debuglevel.greeter.person

import io.micronaut.core.annotation.Introspected

@Introspected
data class PersonRequest(
    var name: String
)