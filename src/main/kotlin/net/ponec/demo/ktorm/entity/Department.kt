package net.ponec.demo.ktorm.entity


import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/** Entity */
interface Department : Entity<Department> {
    companion object : Entity.Factory<Department>()

    var id: Long
    var name: String
}

/** Table */
object Departments : Table<Department>("department") {
    var id = long("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }
}

/**
 * Return a default entity sequence of Table
 */
val Database.departments get() = this.sequenceOf(Departments)