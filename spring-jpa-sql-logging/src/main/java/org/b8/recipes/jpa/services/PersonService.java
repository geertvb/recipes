package org.b8.recipes.jpa.services;

import org.b8.recipes.jpa.enitities.Person;
import org.b8.recipes.jpa.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    protected PersonRepository personRepository;

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

}
