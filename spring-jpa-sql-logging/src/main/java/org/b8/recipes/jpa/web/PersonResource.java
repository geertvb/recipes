package org.b8.recipes.jpa.web;

import org.b8.recipes.jpa.enitities.Person;
import org.b8.recipes.jpa.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = "/persons")
@RestController
public class PersonResource {

    @Autowired
    protected PersonService personService;

    @GetMapping
    public List<Person> getPersons() {
        return personService.getPersons();
    }

}
