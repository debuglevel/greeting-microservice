package de.debuglevel.greeting.rest

import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import spark.Service
import spark.Spark
import java.io.IOException
import java.net.ServerSocket

object SparkTestUtils {
    /**
     * Shutdown spark server and wait until Spark is finally shut down.
     *
     * This is actually a rather nasty workaround to close Spark after test and ensure that it's down when we exit this method (the actual stopping is done in a separate Thread; so we have no notification about that).
     * But even this does not necessarily catch all race conditions.
     */
    fun awaitShutdown() {
        Spark.stop()

        while (isLocalPortInUse(Configuration.configuration[Key("port", intType)])) {
            Thread.sleep(100)
        }

        while (isSparkInitialized()) {
            Thread.sleep(100)
        }
    }

    /**
     * Access the internals of Spark to check if the "initialized" flag is already set to false.
     */
    private fun isSparkInitialized(): Boolean {

        val sparkClass = Spark::class.java
        val getInstanceMethod = sparkClass.getDeclaredMethod("getInstance")
        getInstanceMethod.isAccessible = true
        val service = getInstanceMethod.invoke(null) as Service

        val serviceClass = service::class.java
        val initializedField = serviceClass.getDeclaredField("initialized")
        initializedField.isAccessible = true
        val initialized = initializedField.getBoolean(service)

        return initialized
    }

    /**
     * Check if the Spark port could again be opened; if not, it is still in use by Spark.
     */
    private fun isLocalPortInUse(port: Int): Boolean {
        return try {
            // ServerSocket try to open a LOCAL port
            ServerSocket(port).close()
            // local port can be opened, it's available
            false
        } catch (e: IOException) {
            // local port cannot be opened, it's in use
            true
        }
    }
}