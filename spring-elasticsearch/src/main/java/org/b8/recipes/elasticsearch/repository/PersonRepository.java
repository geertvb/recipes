package org.b8.recipes.elasticsearch.repository;

import org.b8.recipes.elasticsearch.model.PersonEntity;
import org.springframework.data.repository.CrudRepository;


public interface PersonRepository extends CrudRepository<PersonEntity, String>  {

}
