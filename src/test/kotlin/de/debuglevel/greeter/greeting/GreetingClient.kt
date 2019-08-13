package de.debuglevel.greeter.greeting

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single

@Client("/greetings")
interface GreetingClient {

    @Get
    fun getOne(): Single<String>
}