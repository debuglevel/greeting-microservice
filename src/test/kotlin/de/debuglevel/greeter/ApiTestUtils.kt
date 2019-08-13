package de.debuglevel.greeter

import spark.utils.IOUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


// TODO: replace this Utils with an easy to use REST library
object ApiTestUtils {
    fun request(method: String, path: String, requestBody: String?): TestResponse? {

        val url = URL("http://localhost:4567$path")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method

        if (requestBody != null) {
            // set the content length of the body
            connection.setRequestProperty("Content-length", requestBody.toByteArray().size.toString())
            connection.doOutput = true
            connection.useCaches = false

            // send the string as body of the request
            val outputStream = connection.outputStream
            outputStream.write(requestBody.toByteArray())
            outputStream.close()
        }

        try {
            connection.connect()
            val body = IOUtils.toString(connection.inputStream)
            return TestResponse(
                connection.responseCode,
                connection.contentType,
                body
            )
        } catch (e: IOException) {
            val body = IOUtils.toString(connection.errorStream)
            return TestResponse(
                connection.responseCode,
                connection.contentType,
                body
            )
        }
    }

    class TestResponse(val status: Int, val contentType: String, val body: String?) {
//        fun json(): Map<String, String> {
//            return Gson().fromJson<HashMap<*, *>>(body, HashMap<*, *>::class.java)
//        }
    }
}
