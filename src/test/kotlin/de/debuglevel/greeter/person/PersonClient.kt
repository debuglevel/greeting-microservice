package de.debuglevel.greeter.person

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import javax.validation.constraints.NotBlank

@Client("/persons")
interface PersonClient {
    @Get("/{id}")
    fun getOne(@NotBlank id: Long): Single<PersonDTO>

    @Post("/{name}")
    fun postOne(@NotBlank name: String): Single<PersonDTO>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<PersonDTO>
}