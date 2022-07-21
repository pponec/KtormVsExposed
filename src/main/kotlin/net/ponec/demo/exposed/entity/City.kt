package net.ponec.demo.exposed.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/** Table */
object Cities : LongIdTable("city") {
    val name = varchar("name", 50)
    var stateId = reference("state_id", States.id)
}

/** Entity */
class City(
    id: EntityID<Long>) : Entity<Long>(id
) {
    companion object : EntityClass<Long, City>(Cities)
    var name by Cities.name
    var state by State referencedOn Cities.stateId
}
