package net.ponec.demo.ktorm.entity

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDate


/** Entity
 * We need to define entity classes firstly and bind table objects to them */
interface Employee : Entity<Employee> {
    companion object : Entity.Factory<Employee>()

    var id: Long
    var name: String
    var supervisor: Employee?
    var department: Department
    var city: City
    var contractDay: LocalDate?
}

/** Table */
class Employees(alias: String? = null) : Table<Employee>("employee", alias) {
    override fun aliased(alias: String) = Employees(alias)

    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val supervisorId = long("supervisor_id").bindTo { it.supervisor?.id }  // pop: No reference() ?
    val departmentId = long("department_id").references(Departments) { it.department }
    val cityId = long("city_id").references(Cities) { it.city }
    val contractDay = date("contract_day").bindTo { it.contractDay }
}

/**
 * Default instance of the model.
 */
object DbConstants {
    internal val INSTANCE = Employees()
}

/**
 * Return a default entity sequence of Table
 */
val Database.employees get() = this.sequenceOf(DbConstants.INSTANCE)