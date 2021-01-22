package de.debuglevel.greeter.person

import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import java.util.*
import javax.validation.constraints.NotBlank

@Client("/persons")
interface PersonClient {
    @Get("/{id}")
    fun get(@NotBlank id: UUID): Single<GetPersonResponse>

    @Post("/")
    fun add(@Body person: AddPersonRequest): Single<AddPersonResponse>

    @Put("/{id}")
    fun update(@NotBlank id: UUID, @Body person: UpdatePersonRequest): Single<UpdatePersonResponse>

    @Get("/")
    fun list(): List<GetPersonResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetPersonResponse>
}