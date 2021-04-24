package de.debuglevel.greeter.greeting

import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class GreetingEndpoint(
    private val greetingService: GreetingService
) : GreetingGrpcKt.GreetingCoroutineImplBase() {
    private val logger = KotlinLogging.logger {}

    override suspend fun greet(request: GreetRequest): GreetReply {
        logger.debug("Called greet($request)")

        val greeting = greetingService.greet(request.name, request.locale)
        val greetReply = GreetReply.newBuilder().setMessage(greeting.greeting).build()
        return greetReply
    }
}