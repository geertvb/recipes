package org.b8.recipes.jpa.repositories;

import org.b8.recipes.jpa.enitities.Person;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ListCrudRepository<Person, String> {
}
