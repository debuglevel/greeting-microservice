package de.debuglevel.greeter

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.IOException

object JsonUtils {
    fun isValidJson(json: String): Boolean {
        var valid = false
        try {
            val parser = ObjectMapper()
                .factory
                .createParser(json)
            while (parser.nextToken() != null) {
            }
            valid = true
        } catch (ex: JsonParseException) {
//            ex.printStackTrace()
        } catch (ex: IOException) {
//            ex.printStackTrace()
        }

        return valid
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonUtilsTest {
    @Test
    fun `valid JSON`() {
        // Arrange
        val json = """
            {
              "Herausgeber": "Xema",
              "Nummer": "1234-5678-9012-3456",
              "Deckung": 2e+6
            }
        """.trimIndent()

        // Act
        val isValid = JsonUtils.isValidJson(json)

        // Assert
        Assertions.assertThat(isValid).isTrue()
    }

    @Test
    fun `invalid JSON`() {
        // Arrange
        val json = """
            {
              "Herausgeber": "Xema",
              "Nummer": "1234-5678-9012-3456"
              "Deckung": 2e+6
        """.trimIndent()

        // Act
        val isValid = JsonUtils.isValidJson(json)

        // Assert
        Assertions.assertThat(isValid).isFalse()
    }
}