//package de.debuglevel.greeter
//
//import com.natpryce.konfig.Configuration
//import com.natpryce.konfig.ConfigurationProperties
//import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
//import com.natpryce.konfig.EnvironmentVariables
//import com.natpryce.konfig.overriding
//import java.io.File
//
//object Configuration {
//    val configuration: Configuration
//
//    init {
//        var config: Configuration = systemProperties()
//
//        config = config overriding
//                EnvironmentVariables()
//
//        config = config overriding
//                ConfigurationProperties.fromOptionalFile(File("configuration.properties"))
//
//        val defaultsPropertiesFilename = "# TODO does not work. obviously the file should be named otherwise :D\nmicronaut.server.port=80"
//        if (ClassLoader.getSystemClassLoader().getResource(defaultsPropertiesFilename) != null) {
//            config = config overriding
//                    ConfigurationProperties.fromResource(defaultsPropertiesFilename)
//        }
//
//        configuration = config
//    }
//
//    //val mongodbUrl = configuration.getOrNull(Key("mongodb.url", stringType)) ?: "localhost:27017"
//}