package org.b8.recipes.jpa.enitities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Person {

    @Id
    private String id;

    private String name;

}
