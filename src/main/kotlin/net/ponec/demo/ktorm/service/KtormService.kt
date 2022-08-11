package net.ponec.demo.ktorm.service

import net.ponec.demo.controller.EmployeeDto


interface KtormService {

    fun initDatabase()

    /** Return all employes */
    fun findAllEmployeesByEntity() : List<EmployeeDto>

    fun findAllEmployeesByTable(): List<EmployeeDto>
}

