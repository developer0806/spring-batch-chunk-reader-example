package org.my.experiments;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@Log4j2
public class Application {

    public static void main(String[] args) throws Exception {
        log.info("starting application");
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        Job copyPersonsJob = (Job) run.getBean("copyPersonsJob");
        JobLauncher jobLauncher = (JobLauncher) run.getBean("jobLauncher");
        JobExecution jobExecution = jobLauncher.run(copyPersonsJob, new JobParameters());

        log.info("stopping application jobExecution.getJobId() [{}], [{}], [{}]" ,
                jobExecution.getJobId(),
                jobExecution.getStatus(),
                jobExecution.getExitStatus());
    }
}