package org.my.experiments.batchprocessing;

import org.my.experiments.batchprocessing.model.Person;
import org.my.experiments.batchprocessing.processor.PersonProcessor;
import org.my.experiments.batchprocessing.reader.PersonItemReader;
import org.my.experiments.batchprocessing.task.PersonPopulaterTask;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

/*
    @Bean
    public PersonItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("persons.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }
*/

    @Bean
    public PersonItemReader reader() {
        return new PersonItemReader();
    }

    @Bean
    public PersonProcessor processor() {
        return new PersonProcessor();
    }

    @Bean
    public FlatFileItemWriter<Person> writer() {
        LineAggregator<Person> lineAggregator = new PassThroughLineAggregator<>();
        return new FlatFileItemWriterBuilder<Person>()
                .name("personWriter")
                .resource(new FileSystemResource("./output.csv"))
                .append(false)
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    public Job copyPersonsJob(Step step0, Step step1) {
        return jobBuilderFactory.get("copyPersonJob")
                .flow(step0)
                .next(step1)
                .end().build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Step step0() {
        return stepBuilderFactory.get("step0")
                .tasklet(populaterTask())
                .build();
    }

    @Bean
    public PersonPopulaterTask populaterTask() {
        return new PersonPopulaterTask();
    }
}
