package org.my.experiments.batchprocessing.task;

import lombok.extern.log4j.Log4j2;
import org.my.experiments.batchprocessing.model.Person;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class PersonPopulaterTask implements Tasklet, StepExecutionListener {

    public static final String COMMA = ",";
    public static final String PERSONS_CSV = "src/main/resources/persons.csv";
    List<Person> personList;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(PERSONS_CSV));
        personList = reader.lines().map(line -> {
            String[] split = line.split(COMMA);
            return new Person(split[0], split[1]);
        }).collect(Collectors.toList());
        log.info("Finished reading [{}] persons", personList.size());
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        personList = new ArrayList<>();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put("persons", personList);
        return ExitStatus.COMPLETED;
    }
}
