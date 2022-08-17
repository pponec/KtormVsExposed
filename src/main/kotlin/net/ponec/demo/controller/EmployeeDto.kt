package net.ponec.demo.controller

import java.time.LocalDate

/** DTO object */
class EmployeeDto(
    var id: Long,
    var name: String,
    var supervisor: String?,
    var city: String,
    var country: String,
    var department: String,
    var contractDay: LocalDate?
)
