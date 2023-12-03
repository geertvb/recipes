package org.b8.recipes.elasticsearch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Data
@EqualsAndHashCode(of = "id")
@Document(indexName = "person")
public class PersonEntity {

    @Id
    @Field(type = Keyword)
    private String id;

    @Version
    @Field(type = FieldType.Long)
    private Long version;

    @Field(type = Keyword)
    private String firstName;

    @Field(type = Keyword)
    private String lastName;

}
