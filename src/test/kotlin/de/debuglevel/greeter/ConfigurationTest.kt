//package de.debuglevel.greeter
//
//import com.natpryce.konfig.Key
//import com.natpryce.konfig.intType
//import de.debuglevel.greeter.Configuration
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ConfigurationTest {
//
//    @Test
//    fun `port is test port 4567`() {
//        // Arrange
//
//        // Act
//        val port = Configuration.configuration[Key("port", intType)]
//
//        // Assert
//        assertThat(port).isEqualTo(4567)
//    }
//}