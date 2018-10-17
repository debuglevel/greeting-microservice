package de.debuglevel.greeting.rest

import com.google.gson.Gson

object JsonUtils {
    private val gson = Gson()

    fun isJSONValid(jsonInString: String?): Boolean {
        return try {
            gson.fromJson(jsonInString, Any::class.java)
            true
        } catch (ex: Exception) {
            false
        }
    }
}