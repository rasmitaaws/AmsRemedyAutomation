package com.cpa.automation.demo.job;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TeamRemedyAutomationJobRunner {

	private JobLauncher simpleJobLauncher;

	private Job teamRemedyAutomationJob;

	@Autowired
	public TeamRemedyAutomationJobRunner(Job teamRemedyAutomationJob, JobLauncher jobLauncher) {
		this.simpleJobLauncher = jobLauncher;
		this.teamRemedyAutomationJob = teamRemedyAutomationJob;
	}

	@Async
	public void runBatchJob() {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

		jobParametersBuilder.addDate("date", new Date(), true);
		runJob(teamRemedyAutomationJob, jobParametersBuilder.toJobParameters());
	}

	public void runJob(Job job, JobParameters parameters) {
		try {

			JobExecution jobExecution = simpleJobLauncher.run(job, parameters);

			// System.out.println("Exit Status " + jobExecution.getExitStatus());

			System.out.println(jobExecution.getAllFailureExceptions());

			// jobExecution.stop();

			Thread.sleep(5000);

			System.out.println(jobExecution.isStopping());

		} catch (JobExecutionAlreadyRunningException e) {
			/*
			 * logger.info("Job with fileName={} is already running.",
			 * parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
			 */
		} catch (JobRestartException e) {
			/*
			 * logger.info("Job with fileName={} was not restarted.",
			 * parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
			 */
		} catch (JobInstanceAlreadyCompleteException e) {
			/*
			 * logger.info("Job with fileName={} already completed.",
			 * parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
			 */
		} catch (JobParametersInvalidException e) {
			// logger.info("Invalid job parameters.",
			// parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
		} catch (Exception e) {
			// logger.info("Invalid job parameters.",
			// parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
		}
	}

}
