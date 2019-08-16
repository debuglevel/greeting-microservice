package de.debuglevel.greeter.greeting

/**
 * A greeting
 *
 * @param name name of the person to greeting
 * @constructor the `localizedGreeting` field is annotated with `@Transient` so that it is excluded from serialization
 */
data class Greeting(
    @Transient
    private val localizedGreeting: String,
    private val name: String
) {
    val greeting: String = localizedGreeting.format(name)
}