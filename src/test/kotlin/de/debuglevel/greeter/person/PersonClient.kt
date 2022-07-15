package de.debuglevel.greeter.person

import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.constraints.NotBlank

@Client("/persons")
interface PersonClient {
    @Get("/{id}")
    fun get(@NotBlank id: UUID): Mono<GetPersonResponse>

    // TODO: Should probably be a reactive Flux<> instead
    @Get("/")
    fun getAll(): List<GetPersonResponse>

    @Post("/")
    fun add(@Body person: AddPersonRequest): Mono<AddPersonResponse>

    @Put("/{id}")
    fun update(@NotBlank id: UUID, @Body person: UpdatePersonRequest): Mono<UpdatePersonResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetPersonResponse>
}