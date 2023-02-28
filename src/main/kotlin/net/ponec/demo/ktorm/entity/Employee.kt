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
    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val supervisorId = long("supervisor_id").bindTo { it.supervisor?.id }  // pop: No reference ?
    val departmentId = long("department_id").references(Departments.instance) { it.department }
    val cityId = long("city_id").references(Cities.instance) { it.city }
    val contractDay = date("contract_day").bindTo { it.contractDay }

    // Optional relations:
    val department = departmentId.referenceTable as Departments
    val city = cityId.referenceTable as Cities
    // NPE: Cyclic reference binding is not supported  - using the EntitySequence:
    // https://github.com/kotlin-orm/ktorm/discussions/425#discussioncomment-3476971
    // val supervisor = supervisorId.referenceTable as Employees

    override fun aliased(alias: String) = Employees(alias)

    // Helper methods
    companion object {
        val instance = Employees()
    }
}

/**
 * Return a default entity sequence of Table
 */
val Database.employees get() = this.sequenceOf(Employees.instance)