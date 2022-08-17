package net.ponec.demo.controller

import net.ponec.demo.exposed.service.ExposedService
import net.ponec.demo.ktorm.service.KtormService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class DemoRest(
    private val exposedService: ExposedService,
    private val ktormService: KtormService

) {
    @GetMapping("hello")
    fun hello(@RequestParam(required = false) name: String): String {
        return "Hallo $name"
    }

    /** Call an Exposed service.
     * URL: http://localhost:8080/employeesExpByEntity */
    @GetMapping(value = ["employeesExpByEntity"])
    fun findAllEmployeesByEntity(): List<EmployeeDto> {
        return exposedService.findAllEmployeesByEntity()
    }

    /** Call an Exposed service.
     * URL: http://localhost:8080/employeesExpByTable */
    @GetMapping(value = ["employeesExpByTable"])
    fun findAllEmployeesByTable(): List<EmployeeDto> {
        return exposedService.findAllEmployeesByTable()
    }

    /** Call a Ktorm service.
     * URL: http://localhost:8080/employeesKtormByEntity  */
    @GetMapping(value = ["employeesKtormByEntity"])
    fun findAllEmployees2(): List<EmployeeDto> {
        return ktormService.findAllEmployeesByEntity()
    }

    /** Call a Ktorm service.
     * URL: http://localhost:8080/employeesKtormByTable  */
    @GetMapping(value = ["employeesKtormByTable"])
    fun findAllEmployees3(): List<EmployeeDto> {
        return ktormService.findAllEmployeesByTable()
    }
}