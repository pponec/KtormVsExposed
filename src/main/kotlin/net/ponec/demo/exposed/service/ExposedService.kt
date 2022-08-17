package net.ponec.demo.exposed.service

import net.ponec.demo.controller.EmployeeDto


interface ExposedService {

    /** Return all employes by entity */
    fun findAllEmployeesByEntity() : List<EmployeeDto>

    /** Return all employes by table */
    fun findAllEmployeesByTable(): List<EmployeeDto>
}

