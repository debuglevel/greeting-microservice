package de.debuglevel.greeter.grpc

import io.grpc.*
import io.micronaut.context.annotation.Property
import io.micronaut.core.order.Ordered
import mu.KotlinLogging
import java.util.*
import javax.inject.Singleton

@Singleton
class BasicAuthInterceptor(
    @Property(name = "micronaut.security.enabled") val enabled: Boolean,
    @Property(name = "app.security.configurable-credential-authentication.username") val username: String,
    @Property(name = "app.security.configurable-credential-authentication.password") val password: String,
) : ServerInterceptor, Ordered {
    private val logger = KotlinLogging.logger {}

    override fun getOrder(): Int {
        return 10
    }

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        if (!enabled) {
            logger.trace { "Skipping authentication because security is disabled" }
            return next.startCall(call, headers)
        }

        logger.debug { "Authenticating request..." }

        val basicAuthHeaderValue = headers.get(BasicAuthCallCredentials.AUTHORIZATION_KEY_HEADER)
        val base64encoded = basicAuthHeaderValue?.substringAfter("Basic ")
        val credentialsByteArray = Base64.getDecoder().decode(base64encoded) ?: null
        val credentials = credentialsByteArray?.let { String(it) }
        val requestUsername = credentials?.substringBefore(":")
        val requestPassword = credentials?.substringAfter(":")

        if (requestUsername == username && requestPassword == password
        ) {
            logger.debug { "Authentication succeeded for user '$requestUsername'" }
        } else {
            logger.debug { "Authentication failed for user '$requestUsername'" }
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid credentials given in Basic Auth"), headers)
        }

        return next.startCall(call, headers)
    }
}