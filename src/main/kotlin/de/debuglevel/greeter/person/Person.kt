package de.debuglevel.greeter.person

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@Introspected
data class Person(
    @Id
    @GeneratedValue
    var id: UUID?,
    var name: String
)