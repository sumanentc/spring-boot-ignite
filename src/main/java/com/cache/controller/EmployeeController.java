package com.cache.controller;

import com.cache.entity.Employee;
import com.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "employee/{id}", method = RequestMethod.GET)
    public Employee findEmployeeById(@PathVariable String id)
    {
        System.out.println("Searching by ID  : " + id);
        return employeeService.getEmployeeByID(id);
    }

    @RequestMapping(value = "employee/{id}/{name}", method = RequestMethod.PUT)
    public Employee findEmployeeById(@PathVariable String id,@PathVariable String name)
    {
        System.out.println("Updating by ID  : " + id);
        return employeeService.updateEmployee(id,name);
    }

    @RequestMapping(value = "employee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployeeById(@PathVariable String id)
    {
        System.out.println("Updating by ID  : " + id);
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
