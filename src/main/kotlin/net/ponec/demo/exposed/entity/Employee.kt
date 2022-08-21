package net.ponec.demo.exposed.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

/** Table
 * see : https://github.com/JetBrains/Exposed/wiki/DAO
 * see :  https://stackoverflow.com/questions/66109368/kotlin-exposed-create-entity-with-reference */
object Employees : LongIdTable("employee") {
    val name = varchar("name", 50)
    val supervisorId = optReference("supervisor_id", Employees.id)
    val departmentId = reference("department_id", Departments.id)
    val cityId = reference("city_id", Cities.id)
    val contractDay = date("contract_day").nullable()
}

/** Entity */
class Employee(
    id: EntityID<Long>
) : Entity<Long>(id) {
    companion object : EntityClass<Long, Employee>(Employees)

    var name by Employees.name
    var supervisor by Employee optionalReferencedOn Employees.supervisorId
    var department by Department referencedOn Employees.departmentId
    var city by City referencedOn Employees.cityId
    var contractDay by Employees.contractDay
}