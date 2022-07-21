package net.ponec.demo.exposed.controller;

import net.ponec.demo.exposed.entity.Department;
import net.ponec.demo.exposed.entity.Employee;
import net.ponec.demo.exposed.entity.EmployeeDto;
import net.ponec.demo.exposed.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController("/")
public class DemoRest {

    private final DataService dataService;

    public DemoRest(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("hello")
    public String hello(@RequestParam(required = false) String name) {
        return "Hallo " + name;
    }

    /** URL: http://localhost:8080/employees */
    @GetMapping(value = "employees")
    public List<EmployeeDto> findAllEmployees() {
        return dataService.findAllEmployees();
    }


}
