package net.ponec.demo.ktorm.entity

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar


/** Entity
 * We need to define entity classes firstly and bind table objects to them */
interface Employee : Entity<Employee> {
    companion object : Entity.Factory<Employee>()
    var id: Long
    var name: String
    var city: City
    var department: Department
}


/** Table */
object Employees : Table<Employee>("employee") {
    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val cityId = long("city_id").references(Cities) { it.city }
    val departmentId = long("department_id").references(Departments) { it.department }
}

/**
 * Return a default entity sequence of Table
 */
val Database.employees get() = this.sequenceOf(Employees)