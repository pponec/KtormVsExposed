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
class Departments(alias: String? = null) : Table<Department>("department", alias) {
    var id = long("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }

    override fun aliased(alias: String) = Departments(alias)

    // Helper methods
    companion object {
        val instance = Departments()
    }
}

/**
 * Return a default entity sequence of Table
 */
val Database.departments get() = this.sequenceOf(Departments.instance)