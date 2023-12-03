package org.b8.recipes.elasticsearch.web;

import org.b8.recipes.elasticsearch.model.PersonEntity;
import org.b8.recipes.elasticsearch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/persons")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping
    public void createPerson(@RequestBody PersonEntity personEntity) {
        personRepository.save(personEntity);
    }

}
