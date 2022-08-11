package net.ponec.demo.exposed.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/** Table */
object Countries : LongIdTable("country") {
    val name = varchar("name", 50)
}

/** Entity */
class Country(
    id: EntityID<Long>) : Entity<Long>(id
) {
    companion object : EntityClass<Long, Country>(Countries)
    var name by Countries.name
}
