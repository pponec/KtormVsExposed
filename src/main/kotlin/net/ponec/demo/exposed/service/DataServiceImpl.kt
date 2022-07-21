package net.ponec.demo.exposed.service

import net.ponec.demo.exposed.entity.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.mutableListOf as mutableListOf1


/**
 * See: https://touk-pl.translate.goog/blog/2019/02/12/how-we-use-kotlin-with-exposed-at-touk/?_x_tr_sl=en&_x_tr_tl=cs&_x_tr_hl=cs&_x_tr_pto=wapp
 * See: https://touk.pl/blog/2019/02/12/how-we-use-kotlin-with-exposed-at-touk/
 * See: https://spring.io/blog/2016/03/20/a-geospatial-messenger-with-kotlin-spring-boot-and-postgresql
 * See: https://github.com/JetBrains/Exposed
 * See: https://github.com/bastman/spring-kotlin-exposed
 *
 */
@Service
class DataServiceImpl : DataService {

    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        event.applicationContext.getBean(DataServiceImpl::class.java).initDatabase()
    }

    @Transactional
    fun initDatabase() {
        println(">>> Init database")
        SchemaUtils.createMissingTablesAndColumns(States, Cities, Departments, Employees)
        initDataByDao()
    }

    /** Return all employes
     * @see https://github.com/JetBrains/Exposed
     */
    @Transactional
    override fun findAllEmployees(): List<EmployeeDto> {
        var result = mutableListOf1<EmployeeDto>()
        val employeeQuery = Employees.innerJoin(Cities).innerJoin(States)
            .select { Employees.name.isNotNull() and States.name.isNotNull() }
        val employees = Employee.wrapRows(employeeQuery)

        employees.forEach { employee ->
            println(">>> ${employee.id}: ${employee.name} ${employee.city.state.name}")

            var employeeDto = EmployeeDto(
                id = employee.id.value,
                name = employee.name,
                city = employee.city.name,
                state = employee.city.state.name,
                department = employee.department.name
            )
            result.add(employeeDto)
        }
        return result
    }

    private fun initDataByDao() {

        var stateObj = State.new {
            name = "France"
        }

        var cityObj = City.new {
            name = "Paris"
            state = stateObj
        }

        var departmentObj = Department.new {
            name = "Developers"
        }

        // org.h2.jdbc.JdbcSQLDataException: Serializace selhala, příčina: "java.io.NotSerializableException: org.jetbrains.exposed.dao.DaoEntityID"
        var employee1 = Employee.new {
            name = "Franta"
            city = cityObj
            department = departmentObj
        }

        var employee2 = Employee.new {
            name = "Jirka"
            city = cityObj
            department = departmentObj
        }

        var employee3 = Employee.new {
            name = "Pepa"
            city = cityObj
            department = departmentObj
        }

        println("Users: $employee1, $employee2, $employee3")
    }
}