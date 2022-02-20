package de.debuglevel.greeter.person

import io.micronaut.context.annotation.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("app.greetings.persons")
class PersonProperties {
    var someDuration: Duration = Duration.ofSeconds(1)
    var someText: String = "default"
    var someInteger: Int = 1
    var someDouble: Double = 1.0
}
