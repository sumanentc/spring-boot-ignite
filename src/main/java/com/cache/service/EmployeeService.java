package com.cache.service;

import com.cache.entity.Employee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service
public class EmployeeService {

    private List<Employee> employeeList = new ArrayList<>();

    @Cacheable(cacheNames="employee",key="#id")
    public Employee getEmployeeByID(String id)
    {
        try
        {
            System.out.println(" Will Sleep for 5 Seconds");
            Thread.sleep(1000*5);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Employee employee = new Employee(id,"Suman Das");
        employeeList.add(employee);
        return employee;
    }

    @CachePut(cacheNames="employee", key="#id" , unless="#result==null")
    public Employee updateEmployee(String id,String name) {
        for(Employee e : employeeList){
            if(e.getId().equalsIgnoreCase(id)) {
                e.setName(name);
               return e;
            }
        }
        return null;
    }

    @CacheEvict(value = "employee", key="#id")
    public void deleteEmployee(String id){
        employeeList.removeIf(e -> e.getId().equalsIgnoreCase(id));
    }
}
