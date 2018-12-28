package de.debuglevel.greeting.rest

import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import spark.Spark
import java.io.IOException
import java.net.ServerSocket

object SparkTestUtils {
    fun awaitShutdown() {
        // HACK: nasty workaround to close Spark after test and ensure that it's down when we exit the method (stopping is done in a Thread).
        // But even this does not necessarily catch all race conditions
        Spark.stop()
        while (isLocalPortInUse(Configuration.configuration[Key("port", intType)])) {
            Thread.sleep(100)
        }
    }

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