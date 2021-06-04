package de.debuglevel.greeter.grpc

import io.grpc.CallCredentials
import io.grpc.Metadata
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.Executor

class BasicAuthCallCredentials(
    private val username: String,
    private val password: String,
) : CallCredentials() {
    private val logger = KotlinLogging.logger {}

    override fun applyRequestMetadata(requestInfo: RequestInfo, appExecutor: Executor, applier: MetadataApplier) {
        val headers = Metadata()
        headers.put(Companion.AUTHORIZATION_KEY_HEADER, buildBasicAuthHeaderValue(username, password))
        applier.apply(headers)
    }

    private fun buildBasicAuthHeaderValue(username: String, password: String): String {
        logger.trace { "Building Basic Auth header value..." }

        val base64encoded = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
        val headerValue = "Basic $base64encoded"

        logger.trace { "Built Basic Auth header value: $headerValue" }
        return headerValue
    }

    /**
     * Should be a noop but never called; tries to make it clearer to implementors that they may break
     * in the future.
     */
    override fun thisUsesUnstableApi() {
        TODO("Not yet implemented")
    }

    companion object {
        val AUTHORIZATION_KEY_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
    }
}