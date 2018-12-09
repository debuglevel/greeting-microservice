package de.debuglevel.greeting.rest.responsetransformer

import com.google.gson.Gson
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonTransformerTest {

    /**
     * It would not be very reasonable to test whether the underlying GSON library does it's job.
     * But we can ensure that JsonTransformer serializes into a JSON which can be deserialized to the equal data again.
     */
    @Test
    fun `deserialize serialized JSON`() {
        // Arrange
        val data1 = JsonDataClass(42)
        val data2 = JsonDataClass(4711, "MyName")

        // Act
        val data1json = JsonTransformer.render(data1)
        val data2json = JsonTransformer.render(data2)

        //Assert
        val data1deserialized = Gson().fromJson(data1json, JsonDataClass::class.java)
        Assertions.assertThat(data1deserialized).isEqualTo(data1)
        Assertions.assertThat(data1deserialized).isNotEqualTo(data2)

        val data2deserialized = Gson().fromJson(data2json, JsonDataClass::class.java)
        Assertions.assertThat(data2deserialized).isEqualTo(data2)
        Assertions.assertThat(data2deserialized).isNotEqualTo(data1)
    }

    data class JsonDataClass(val id: Int, val name: String? = null)
}