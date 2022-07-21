package net.ponec.demo.exposed.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/** Table */
object States : LongIdTable("state") {
    val name = varchar("name", 50)
}

/** Entity */
class State(
    id: EntityID<Long>) : Entity<Long>(id
) {
    companion object : EntityClass<Long, State>(States)
    var name by States.name
}
