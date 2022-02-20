package de.debuglevel.greeter.greeting

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono
import javax.validation.constraints.NotBlank

@Client("/greetings")
interface GreetingClient {

    @Get("/{name}{?language}")
    fun getOne(@NotBlank name: String, language: String?): Mono<GreetingDTO>

    @Post("/")
    fun postOne(greetingRequest: GreetingRequest): Mono<GreetingDTO>

//    @Get
//    fun getList(): Set<GreetingDTO>
}