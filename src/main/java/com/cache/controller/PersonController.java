package com.cache.controller;

import com.cache.entity.Person;
import com.cache.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping
    @ResponseBody
    public Person addPerson(@RequestBody Person person){
        return personService.addPerson(person);
    }
    @PutMapping("/{id}")
    public Person updatePerson(@RequestBody Person person,@PathVariable Long id){
        return personService.updatePerson(person,id);
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id){
        return personService.getPerson(id);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id){
        personService.deletePerson(id);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Person> findPersons(@RequestParam(required = false) String firstName, @RequestParam(required = false) Integer age){
            return personService.getPersons(firstName,age);
    }

    @GetMapping("/organization/{organizationName}")
    @ResponseBody
    public List<Person> findPersons(@PathVariable String organizationName){
        return personService.getPersonsByOrganization(organizationName);
    }


}
