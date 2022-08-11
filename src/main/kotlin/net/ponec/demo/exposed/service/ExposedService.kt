package net.ponec.demo.exposed.service

import net.ponec.demo.controller.EmployeeDto


interface ExposedService {

    /** Return all employes */
    fun findAllEmployees() : List<EmployeeDto>
}

