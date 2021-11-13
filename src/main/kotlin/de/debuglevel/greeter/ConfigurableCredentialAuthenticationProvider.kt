package de.debuglevel.greeter

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import jakarta.inject.Singleton
import mu.KotlinLogging
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import java.util.function.Consumer


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
    ): Publisher<AuthenticationResponse?>? {
        logger.debug { "Authenticating user '${authenticationRequest.identity}'..." }

        return Mono.create<AuthenticationResponse>(Consumer<MonoSink<AuthenticationResponse?>> { emitter: MonoSink<AuthenticationResponse?> ->
            if (authenticationRequest.identity == username &&
                authenticationRequest.secret == password
            ) {
                logger.debug { "Authentication succeeded for user '${authenticationRequest.identity}'" }
                emitter.success(AuthenticationResponse.success(authenticationRequest.identity as String))
            } else {
                logger.debug { "Authentication failed for user '${authenticationRequest.identity}'" }
                emitter.error(AuthenticationException(AuthenticationFailed()))
            }
        })
    }
}
