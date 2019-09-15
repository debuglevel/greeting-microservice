package de.debuglevel.greeter.person

import java.util.*

data class PersonRequest(
    val id: UUID?,
    val name: String
)