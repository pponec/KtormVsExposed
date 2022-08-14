package net.ponec.demo.jooq.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/** Table */
object Departments : LongIdTable("department") {
    val name = varchar("name", 50)
}

/** Entity */
class Department(
    id: EntityID<Long>) : Entity<Long>(id
) {
    companion object : EntityClass<Long, Department>(Departments)

    var name by Departments.name
}
