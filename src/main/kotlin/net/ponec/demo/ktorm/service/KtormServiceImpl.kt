package net.ponec.demo.ktorm.service

import net.ponec.demo.controller.EmployeeDto
import net.ponec.demo.ktorm.entity.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
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
        val result = database
            .sequenceOf(Employees, withReferences = true) // or simply: employees
            // TODO:pop How to join related entities?
            .filter { it.departmentId greaterEq 0L }
            .sortedBy { it.name }
            //.sortedBy { Cities.name.asc() } // TODO:pop How to sort result by related columns?
            .take(10) // limit
            .drop(0)  // offset
            .asKotlinSequence() // optional
            .map {
                EmployeeDto(
                    id = it.id,
                    name = it.name,
                    supervisor = it.supervisor?.name,
                    city = it.city.name,
                    country = it.city.country.name,
                    department = it.department.name,
                    contractDay = it.contractDay
                )
            }
            .toList()

        return result
    }

    /** Return all employess by the Table model. */
    @Transactional
    override fun findAllEmployeesByTable(): List<EmployeeDto> {
        val supervisor = Employees.aliased("supervisor")
        val result = database
            .from(Employees)
            .innerJoin(Departments, on = Departments.id eq Employees.departmentId)
            .innerJoin(Cities, on = Cities.id eq Employees.cityId)
            .innerJoin(Countries, on = Countries.id eq Cities.countryId)
            //.innerJoin(supervisor, on = supervisor[Employees.id] eq Employees.supervisorId) // TODO:pop How to circle relations?
            .select()
            .limit(offset = 0, 50)
            .where { Employees.departmentId greaterEq 0L }
            .orderBy(Cities.name.asc(), Employees.name.asc())
            .map {
                EmployeeDto(
                    id = it[Employees.id] ?: 0L,
                    name = it[Employees.name] ?: "",
                    supervisor = /*it[superiors.name] ?:*/ "?",  // TODO:pop how to read a superior name?
                    department = it[Departments.name] ?: "",
                    city = it[Cities.name] ?: "",
                    country = it[Countries.name] ?: "",
                    contractDay = it[Employees.contractDay], // nullable
                )
            }
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
            supervisor = null
            contractDay = LocalDate.of(2020, 2, 10)
        }

        val employee2 = Employee {
            name = "Lucy"
            city = myCity
            department = myDepartment
            supervisor = employee1
            contractDay = LocalDate.of(2020, 2, 11)
        }

        // A different approach to create object:
        val employee3 = Entity.create<Employee>() // or Entity()
        employee3.name = "Elisabeth"
        employee3.city = myCity
        employee3.department = myDepartment
        employee3.supervisor = employee1
        employee3.contractDay = LocalDate.of(2020, 2, 12)


        database.countries.add(myCountry)
        database.cities.add(myCity)
        database.departments.add(myDepartment)
        database.employees.add(employee1)
        database.employees.add(employee2)
        database.employees.add(employee3)

        println("Users by Ktorm: $employee1.id, $employee2.id, $employee3.id")
    }
}