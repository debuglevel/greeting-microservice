package de.debuglevel.greeter.greeting

/**
 * A greeting
 *
 * @param localizedGreeting a greeting containing a "%s" which will be replaced by the name
 * @param name name of the person to greet
 * @constructor the `localizedGreeting` field is annotated with `@Transient` so that it is excluded from serialization
 */
data class Greeting(
    @Transient
    private val localizedGreeting: String,
    private val name: String
) {
    /**
     * Greeting for the given person
     */
    val greeting: String = localizedGreeting.format(name)
}