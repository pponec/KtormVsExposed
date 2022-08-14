package net.ponec.demo.jooq.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

/** Table
 * see : https://github.com/JetBrains/Exposed/wiki/DAO
 * see :  https://stackoverflow.com/questions/66109368/kotlin-exposed-create-entity-with-reference */
object Employees : LongIdTable("employee") {
    val name = varchar("name", 50)
    val cityId = reference("city_id", Cities.id)
    val departmentId = reference("department_id", Departments.id)
}

/** Entity */
class Employee(
    id: EntityID<Long>
) : Entity<Long>(id) {
    companion object : EntityClass<Long, Employee>(Employees)

    var name by Employees.name
    var city by City referencedOn Employees.cityId
    var department by Department referencedOn Employees.departmentId
}