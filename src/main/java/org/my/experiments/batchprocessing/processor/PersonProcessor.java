package org.my.experiments.batchprocessing.processor;

import lombok.extern.log4j.Log4j2;
import org.my.experiments.batchprocessing.model.Person;
import org.springframework.batch.item.ItemProcessor;

import java.util.Locale;

@Log4j2
public class PersonProcessor implements ItemProcessor<Person, Person> {
    @Override
    public Person process(Person person) {
        log.info("Transforming Person [{}]", person);
        return new Person(person.getFirstName().toLowerCase(), person.getLastName().toUpperCase());
    }
}
