package net.ponec.demo.exposed.service

import net.ponec.demo.exposed.entity.EmployeeDto

interface DataService {

    /** Return all employes */
    fun findAllEmployees() : List<EmployeeDto>
}

