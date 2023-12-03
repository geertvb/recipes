package org.b8.recipes.elasticsearch.kafka;

import lombok.extern.slf4j.Slf4j;
import org.b8.recipes.elasticsearch.model.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.System.nanoTime;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

@Slf4j
@Component
@KafkaListener(topics = "persons", batch = "true")
public class PersonListener {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    protected List<PersonEntity> latestPersonList(List<PersonEntity> personList) {
        var personMap = personList.stream()
                .collect(groupingBy(PersonEntity::getId,
                        maxBy(comparing(PersonEntity::getVersion))));
        return personMap.values().stream()
                .map(Optional::get)
                .toList();
    }

    @KafkaHandler
    public void handlePerson(List<PersonEntity> personList) {
        log.info("Handling {} records", personList.size());

        var latestPersonList = latestPersonList(personList);

        log.info("Indexing {} unique persons", latestPersonList.size());

        var queries = latestPersonList.stream()
                .map(person -> new IndexQueryBuilder()
                        .withIndex("persons")
                        .withId(person.getId())
                        .withVersion(person.getVersion())
                        .withObject(person)
                        .build())
                .toList();

        var start = nanoTime();
        elasticsearchOperations.bulkIndex(queries, PersonEntity.class);
        var duration = nanoTime() - start;

        log.info("Indexing took {} nanoseconds", duration);
    }
}
