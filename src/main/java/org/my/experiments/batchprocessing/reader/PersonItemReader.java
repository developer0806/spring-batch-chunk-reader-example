package org.my.experiments.batchprocessing.reader;

import lombok.extern.log4j.Log4j2;
import org.my.experiments.batchprocessing.model.Person;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

@Log4j2
public class PersonItemReader implements ItemReader<Person> {
    private Iterator<Person> personIterator;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        personIterator = ((List<Person>) stepExecution.getJobExecution().getExecutionContext().get("persons")).iterator();
    }

    @Override
    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
       log.info("Reading Person from Person Reader [{}]", personIterator.hasNext());
        return personIterator.hasNext() ? personIterator.next() : null;
    }
}
