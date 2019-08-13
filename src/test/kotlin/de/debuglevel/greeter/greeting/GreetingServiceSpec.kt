package de.debuglevel.greeter.greeting

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object GreetingServiceSpec : Spek({
    describe("GreetingService Suite") {
        val greetingService = GreetingService()

        it("test /hello responds Hello World") {
            val greetingDTO = greetingService.greet("Mozart")
            assertEquals(greetingDTO.greeting, "Hello, Mozart!")
        }
    }
})