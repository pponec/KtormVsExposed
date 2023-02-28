package net.ponec.demo.exposed.service

import net.ponec.demo.controller.EmployeeDto
import net.ponec.demo.exposed.entity.*
import org.jetbrains.exposed.sql.*
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import net.ponec.demo.ktorm.service.KtormService as KtormDataService


/**
 * Exposed service implementation
 *
 * See: https://touk-pl.translate.goog/blog/2019/02/12/how-we-use-kotlin-with-exposed-at-touk/?_x_tr_sl=en&_x_tr_tl=cs&_x_tr_hl=cs&_x_tr_pto=wapp
 * See: https://touk.pl/blog/2019/02/12/how-we-use-kotlin-with-exposed-at-touk/
 * See: https://spring.io/blog/2016/03/20/a-geospatial-messenger-with-kotlin-spring-boot-and-postgresql
 * See: https://github.com/JetBrains/Exposed
 * See: https://github.com/bastman/spring-kotlin-exposed
 */
@Service
class ExposedServiceImpl(
    protected var ktormService: KtormDataService
) : ExposedService {

    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        event.applicationContext.getBean(ExposedServiceImpl::class.java).initDatabase()
    }

    @Transactional
    fun initDatabase() {
        println(">>> Init database")
        SchemaUtils.createMissingTablesAndColumns(Countries, Cities, Departments, Employees)
        initDataByDao()
        ktormService.initDatabase()
    }

    /** Return all employes
     * See: https://github.com/JetBrains/Exposed
     */
    @Transactional
    override fun findAllEmployeesByEntity(): List<EmployeeDto> {
        val result = mutableListOf<EmployeeDto>()
        val employeeQuery = Employees
            .innerJoin(Cities)
            .innerJoin(Countries)
            .select {
                Employees.id greaterEq 1L and
                        Employees.name.isNotNull() and
                        Countries.name.isNotNull()
            }
            .limit(50, offset = 0)
            .orderBy(
                Cities.name to SortOrder.ASC,
                Employees.name to SortOrder.ASC
            )
        val employees = Employee.wrapRows(employeeQuery)

        employees.forEach { employee ->
            println(">>> ${employee.id}: ${employee.name} ${employee.city.country.name}")

            val employeeDto = EmployeeDto(
                id = employee.id.value,
                name = employee.name,
                city = employee.city.name,
                superior = employee.superior?.name,
                country = employee.city.country.name,
                department = employee.department.name,
                contractDay = employee.contractDay
            )
            result.add(employeeDto)
        }
        return result
    }

    /** Alias tutorial: https://github.com/JetBrains/Exposed/wiki/DSL#alias.
     * See example: https://github.com/JetBrains/Exposed/issues/177
     */
    @Transactional
    override fun findAllEmployeesByTable(): List<EmployeeDto> {
        val superior = Employees.alias("superior")
        val result = Employees
            .innerJoin(Departments)
            .innerJoin(Cities)
            .innerJoin(Countries)
            .leftJoin(superior, { superior[Employees.id] }, { Employees.superiorId })
            .select {
                Employees.id greaterEq 1L and
                        Employees.name.isNotNull() and
                        Countries.name.isNotNull()
            }
            .limit(50, offset = 0)
            .orderBy(
                Cities.name to SortOrder.ASC,
                Employees.name to SortOrder.ASC
            )
            .map {
                EmployeeDto(
                    id = it[Employees.id].value,
                    name = it[Employees.name],
                    superior = it[superior[Employees.name]],
                    city = it[Cities.name],
                    country = it[Countries.name],
                    department = it[Departments.name],
                    contractDay = it[Employees.contractDay],
                )
            }
            .toList()
        return result
    }

    private fun initDataByDao() {

        val countryObj = Country.new {
            name = "France"
        }

        val cityObj = City.new {
            name = "Paris"
            country = countryObj
        }

        val departmentObj = Department.new {
            name = "Developers"
        }

        // org.h2.jdbc.JdbcSQLDataException: Serializace selhala, příčina: "java.io.NotSerializableException: org.jetbrains.exposed.dao.DaoEntityID"
        val employee1 = Employee.new {
            name = "George"
            city = cityObj
            department = departmentObj
            superior = null
            contractDay = LocalDate.of(2020, 1, 1)
        }

        val employee2 = Employee.new {
            name = "Francis"
            city = cityObj
            department = departmentObj
            superior = employee1
            contractDay = LocalDate.of(2020, 1, 2)
        }

        val employee3 = Employee.new {
            name = "Joseph"
            city = cityObj
            department = departmentObj
            superior = employee1
            contractDay = LocalDate.of(2020, 1, 3)
        }

        println("Users: $employee1, $employee2, $employee3")
    }
}