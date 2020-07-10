package de.debuglevel.greeter.greeting

import io.micronaut.core.annotation.Introspected

@Introspected
data class GreetingRequest(
    val name: String,
    val language: String?
)
