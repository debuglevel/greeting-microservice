package de.debuglevel.greeter.greeting

import de.debuglevel.greeter.greeting.GreetingGrpc.GreetingBlockingStub
import de.debuglevel.greeter.grpc.BasicAuthCallCredentials
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import mu.KotlinLogging

@Factory
internal class Clients {
    private val logger = KotlinLogging.logger {}

    @Bean
    fun blockingStub(
        @GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel
    ): GreetingBlockingStub {
        logger.trace { "Building GreetingBlockingStub..." }

        val callCredentials = BasicAuthCallCredentials("SECRET_USERNAME", "SECRET_PASSWORD")
        val greetingBlockingStub = GreetingGrpc.newBlockingStub(channel)
            .withCallCredentials(callCredentials)

        logger.trace { "Built GreetingBlockingStub" }
        return greetingBlockingStub
    }
}