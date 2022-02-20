package de.debuglevel.greeter.grpc

import io.grpc.BindableService
import io.grpc.protobuf.services.ProtoReflectionService
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
internal class ReflectionFactory {
    @Singleton
    fun reflectionService(): BindableService {
        return ProtoReflectionService.newInstance()
    }
}