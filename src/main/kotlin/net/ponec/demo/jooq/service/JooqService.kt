package net.ponec.demo.jooq.service

import net.ponec.demo.controller.EmployeeDto


interface JooqService {

    /** Return all employes */
    fun findAllEmployees() : List<EmployeeDto>
}

