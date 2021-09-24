package de.debuglevel.greeter.greeting

import jakarta.inject.Singleton
import mu.KotlinLogging

@Singleton
class GreetingEndpoint(
    private val greetingService: GreetingService
) : GreetingGrpcKt.GreetingCoroutineImplBase() {
    private val logger = KotlinLogging.logger {}

    override suspend fun greet(request: GreetRequest): GreetReply {
        logger.debug("Called greet($request)")

        val greeting = greetingService.greet(request.name, request.locale)
        val greetReply = GreetReply.newBuilder().setMessage(greeting.greeting).build()

        logger.debug("Called greet($request): $greetReply")
        return greetReply
    }
}