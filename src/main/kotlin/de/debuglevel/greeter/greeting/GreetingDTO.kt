package de.debuglevel.greeter.greeting

/**
 * A greeting
 *
 * @param name name of the person to greeting
 * @constructor the `name` field is annotated with `@Transient` so that it is excluded from GSON serialization
 */
data class GreetingDTO(@Transient private val name: String) {
    val greeting: String = "Hello, $name!"
}