package net.ponec.demo.ktorm.service

import net.ponec.demo.controller.EmployeeDto
import net.ponec.demo.ktorm.entity.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.mutableListOf as mutableListOf1

/** Ktorm service implementation */
@Service
class KtormServiceImpl(
    protected val database: Database
) : KtormService {

    @Transactional
    override fun initDatabase() {
        println(">>> Init Ktorm database")
        initDataByDao()
    }

    /** Return all employess by the Entity model. */
    @Transactional
    override fun findAllEmployeesByEntity(): List<EmployeeDto> {
        val result = mutableListOf1<EmployeeDto>()

        val employees: Sequence<Employee> = database.employees
            .filter { it.departmentId greaterEq 0L }
            .sortedBy { it.id.asc() }
            .drop(0) // limit
            .take(10) // offset
            .asKotlinSequence()

        employees.forEach { employee ->
            println(">>> ${employee.id}: ${employee.name} ${employee.city.country.name}")

            val employeeDto = EmployeeDto(
                id = employee.id,
                name = employee.name,
                city = employee.city.name,
                country = employee.city.country.name,
                department = employee.department.name
            )
            result.add(employeeDto)
        }
        return result
    }

    /** Return all employess by the Table model. */
    @Transactional
    override fun findAllEmployeesByTable(): List<EmployeeDto> {
        val result = database
            .from(Employees)
            .innerJoin(Departments, on = Employees.departmentId eq Departments.id)
            .innerJoin(Cities, on = Employees.cityId eq Cities.id)
            .innerJoin(Countries, on = Cities.countryId eq Countries.id)
            .select()
            .limit(offset = 0, 50)
            .where { Employees.departmentId greaterEq 0L }
            .map {  EmployeeDto(
                id = it[Employees.id] ?: 0L,
                name = it[Employees.name] ?: "",
                department = it[Departments.name] ?: "",
                city = it[Cities.name] ?: "",
                country =  it[Countries.name] ?: "",
            )}
            .toList()

        return result
    }

    /** Insert some employees.
     * <br/>
     * NOTE: If we use an auto-increment key in our table, we just need to tell Ktorm
     * which column is the primary key by calling the primaryKey function on the column registration,
     * then the add function will obtain the generated key from the database and fill it
     * into the corresponding property after the insertion completes.
     * <br/>
     * Source: https://www.ktorm.org/en/entity-dml.html#Insert
     */
    private fun initDataByDao() {

        val myCountry = Country {
            name = "USA"
        }
        println(">>> Country: id=${myCountry.id}, name=${myCountry.name}")

        val myCity = City {
            country = myCountry
            name = "New York"
        }

        val myDepartment = Department {
            name = "Technical Support"
        }

        val employee1 = Employee {
            name = "Catherine"
            city = myCity
            department = myDepartment
        }

        val employee2 = Employee {
            name = "Lucy"
            city = myCity
            department = myDepartment
        }

        val employee3 = Employee {
            name = "Elisabeth"
            city = myCity
            department = myDepartment
        }

        database.countries.add(myCountry)
        database.cities.add(myCity)
        database.departments.add(myDepartment)
        database.employees.add(employee1)
        database.employees.add(employee2)
        database.employees.add(employee3)

        println("Users by Ktorm: $employee1.id, $employee2.id, $employee3.id")
    }
}