package net.ponec.demo.ktorm.entity

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/** Entity */
interface City : Entity<City> {
    companion object : Entity.Factory<City>()

    var id: Long
    var name: String
    var country: Country
}

/** Table */
class Cities(alias: String? = null) : Table<City>("city", alias) {
    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val countryId = long("country_id").references(Countries.instance) { it.country }

    // Optional relation(s):
    val country = countryId.referenceTable as Countries

    override fun aliased(alias: String) = Cities(alias)

    // Helper methods
    companion object {
        val instance = Cities()
    }
}

/**
 * Return a default entity sequence of Table
 */
val Database.cities get() = this.sequenceOf(Cities.instance)