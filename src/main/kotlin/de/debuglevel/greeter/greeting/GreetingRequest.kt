package de.debuglevel.greeter.greeting

data class GreetingRequest(
    val name: String,
    val language: String?
)
