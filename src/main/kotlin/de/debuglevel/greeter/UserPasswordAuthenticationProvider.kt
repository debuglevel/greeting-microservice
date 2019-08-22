package de.debuglevel.greeter

import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Singleton

/**
 * Example for an AuthenticationProvider, which just checks for hardcoded credentials
 */
@Singleton
class UserPasswordAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> {
        if (authenticationRequest.identity == "myUser" &&
            authenticationRequest.secret == "secretPassword"
        ) {
            return Flowable.just(UserDetails("myUser", ArrayList()))
        } else {
            return Flowable.just(AuthenticationFailed())
        }
    }
}