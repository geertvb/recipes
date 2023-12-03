package org.b8.recipes.elasticsearch.web;

import lombok.extern.slf4j.Slf4j;
import org.b8.recipes.elasticsearch.model.PersonEntity;
import org.b8.recipes.elasticsearch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/persons")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping
    public void createPerson(@RequestBody PersonEntity personEntity) {
        long start = System.nanoTime();
        String firstName = personEntity.getFirstName();
        for (long i = 1; i < 100; i++) {
            personEntity.setVersion(i);
            personEntity.setFirstName(firstName + "-" + i);
            personRepository.save(personEntity);
        }
        long duration = System.nanoTime() - start;
        log.info("Execution took {} nanos", duration);
    }

}
