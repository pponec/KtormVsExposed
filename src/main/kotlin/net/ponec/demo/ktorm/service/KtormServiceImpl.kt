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
        var result = mutableListOf1<EmployeeDto>()

        val employees: Sequence<Employee> = database.employees
            .filter { it.departmentId greaterEq 0L }
            .sortedBy { it.id.asc() }
            .drop(0) // limit
            .take(10) // offset
            .asKotlinSequence()

        employees.forEach { employee ->
            println(">>> ${employee.id}: ${employee.name} ${employee.city.country.name}")

            var employeeDto = EmployeeDto(
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
            .innerJoin(Departments)
            .innerJoin(Cities)
            .innerJoin(Countries)
            .select()
            .limit(offset = 0, 50)
            .where { Employees.departmentId greaterEq 0L }
            .map {  EmployeeDto(
                id = it.get(Employees.id) ?: 0L,
                name = it.get(Employees.name) ?: "",
                department = it.get(Departments.name) ?: "",
                city = it.get(Cities.name) ?: "",
                country =  it.get(Countries.name) ?: "",
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
        if (true) return

        var country = Country {
            id = 100L
            name = "USA"
        }
        println(">>> Country: id=${country.id}, name=${country.name}")
        database.countries.add(country)

        var city = City {
            id = 200L
            country = country
            name = "New York"
        }
        database.cities.add(city)

        var department = Department {
            id = -3L
            name = "Technical Support"
        }
        database.departments.add(department)

        var employee1 = Employee {
            id = -4L
            name = "Catherine"
            city = city
            department = department
        }

        var employee2 = Employee {
            id = -5L
            name = "Lucy"
            city = city
            department = department
        }

        var employee3 = Employee {
            id = -6L
            name = "Elisabeth"
            city = city
            department = department
        }

        database.employees.add(employee1)
        database.employees.add(employee2)
        database.employees.add(employee3)


        println("Users: \$employee1, \$employee2, \$employee3")
    }
}