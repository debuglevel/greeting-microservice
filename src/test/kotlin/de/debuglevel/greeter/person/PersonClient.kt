package de.debuglevel.greeter.person

import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import java.util.*
import javax.validation.constraints.NotBlank

@Client("/persons")
interface PersonClient {
    @Get("/{id}")
    fun getOne(@NotBlank id: UUID): Single<GetPersonResponse>

    @Post("/")
    fun postOne(@Body person: AddPersonRequest): Single<AddPersonResponse>

    @Put("/{id}")
    fun putOne(@NotBlank id: UUID, @Body person: UpdatePersonRequest): Single<UpdatePersonResponse>

    @Get("/")
    fun getAll(): List<GetPersonResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetPersonResponse>
}