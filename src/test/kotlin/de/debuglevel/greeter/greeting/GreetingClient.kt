package de.debuglevel.greeter.greeting

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import javax.validation.constraints.NotBlank

@Client("/greetings")
interface GreetingClient {

    @Get("/{name}{?language}")
    fun getOne(@NotBlank name: String, language: String?): Single<GreetingDTO>

    @Post("/")
    fun postOne(greetingRequest: GreetingRequest): Single<GreetingDTO>

//    @Get
//    fun getList(): Set<GreetingDTO>
}