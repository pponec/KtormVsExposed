package net.ponec.demo.jooq.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/** Table */
object Cities : LongIdTable("city") {
    val name = varchar("name", 50)
    var countryId = reference("country_id", Countries.id)
}

/** Entity */
class City(
    id: EntityID<Long>) : Entity<Long>(id
) {
    companion object : EntityClass<Long, City>(Cities)
    var name by Cities.name
    var country by Country referencedOn Cities.countryId
}
