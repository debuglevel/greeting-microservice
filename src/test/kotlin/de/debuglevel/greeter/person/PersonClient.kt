package de.debuglevel.greeter.person

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
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

    @Get("/")
    fun getAll(): Set<GetPersonResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetPersonResponse>
}