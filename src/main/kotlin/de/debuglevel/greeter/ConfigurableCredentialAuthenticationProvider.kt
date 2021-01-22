package de.debuglevel.greeter

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import mu.KotlinLogging
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Singleton

/**
 * Example for an AuthenticationProvider, which just checks for a single configured credential
 */
@Singleton
class ConfigurableCredentialAuthenticationProvider(
    @Property(name = "app.security.configurable-credential-authentication.username") val username: String,
    @Property(name = "app.security.configurable-credential-authentication.password") val password: String
) : AuthenticationProvider {
    private val logger = KotlinLogging.logger {}

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        logger.debug { "Authenticating user '${authenticationRequest.identity}'..." }

        return Flowable.create({ emitter: FlowableEmitter<AuthenticationResponse> ->
            if (authenticationRequest.identity == username &&
                authenticationRequest.secret == password
            ) {
                logger.debug { "Authentication succeeded for user '${authenticationRequest.identity}'" }
                emitter.onNext(UserDetails(authenticationRequest.identity as String, ArrayList()))
            } else {
                logger.debug { "Authentication failed for user '${authenticationRequest.identity}'" }
                emitter.onError(AuthenticationException(AuthenticationFailed()))
            }
            emitter.onComplete()
        }, BackpressureStrategy.ERROR)
    }
}
