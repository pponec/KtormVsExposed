package net.ponec.demo.ktorm.service

import net.ponec.demo.controller.EmployeeDto
import net.ponec.demo.ktorm.entity.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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
        if (false) {
            val result2 = database.sequenceOf(Employees())
                .filter { (it.superiorId.referenceTable as Employees).name eq "John" } // NPE (?)
                .toList()
            println("Count: ${result2.size}")
        }

        val result = database
            .sequenceOf(Employees())
            .filter { it.departmentId greaterEq 0L }
            .sortedBy({ it.city.name.asc() }, { it.name.asc() })
            .take(10) // limit
            .drop(0)  // offset
            .asKotlinSequence() // optional
            .map {
                EmployeeDto(
                    id = it.id,
                    name = it.name,
                    superior = it.superior?.name,
                    city = it.city.name,
                    country = it.city.country.name,
                    department = it.department.name,
                    contractDay = it.contractDay
                )
            }
            .toList()

        return result
    }

    /** Return all employess by the Table model.
     *
     * Example: https://androidrepo.com/repo/kotlin-orm-ktorm
     * */
    @Transactional
    override fun findAllEmployeesByTable(): List<EmployeeDto> {
        val employees = Employees() // pop: Employees must not be singleton (!)
        val superiors = employees.aliased("superior")
        val result = database
            .from(employees)
            .innerJoin(Departments.instance, on = employees.departmentId eq Departments.instance.id)
            .innerJoin(Cities.instance, on = employees.cityId eq Cities.instance.id)
            .innerJoin(Countries.instance, on = Cities.instance.countryId eq Countries.instance.id)
            .leftJoin(superiors, on = employees.superiorId eq superiors.id) // TODO:pop How to circle relations?
            .select(
                employees.id,
                employees.name,
                superiors.name,
                Departments.instance.name,
                Cities.instance.name,
                Countries.instance.name,
                employees.contractDay
            )
            .limit(offset = 0, 50)
            .where { employees.departmentId greaterEq 0L }
            .orderBy(Cities.instance.name.asc(), employees.name.asc())
            .map {
                EmployeeDto(
                    id = it[employees.id] ?: 0L,
                    name = it[employees.name] ?: throw NoSuchElementException(),
                    superior = it[superiors.name],
                    department = it[Departments.instance.name] ?: "",
                    city = it[Cities.instance.name] ?: "",
                    country = it[Countries.instance.name] ?: "",
                    contractDay = it[employees.contractDay], // nullable
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
            superior = null
            contractDay = LocalDate.of(2020, 2, 10)
        }

        val employee2 = Employee {
            name = "Lucy"
            city = myCity
            department = myDepartment
            superior = employee1
            contractDay = LocalDate.of(2020, 2, 11)
        }

        // A different approach to create object:
        val employee3 = Entity.create<Employee>() // or Entity()
        employee3.name = "Elisabeth"
        employee3.city = myCity
        employee3.department = myDepartment
        employee3.superior = employee1
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