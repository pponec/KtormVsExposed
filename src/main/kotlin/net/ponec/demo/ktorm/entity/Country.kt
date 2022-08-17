package net.ponec.demo.ktorm.entity

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/** Entity */
interface Country : Entity<Country> {
    companion object : Entity.Factory<Country>()

    var id: Long
    var name: String
}

/** Table */
object Countries : Table<Country>("country") {
    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

/**
 * Return a default entity sequence of Table
 */
val Database.countries get() = this.sequenceOf(Countries)
