package com.cpa.automation.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cpa.automation.demo.job.TeamRemedyAutomationJobRunner;

@Configuration
@ComponentScan
@EnableBatchProcessing
public class AmsRemedyAutomationApplication implements CommandLineRunner {

	@Autowired
	private TeamRemedyAutomationJobRunner jobRunner;

	public static void main(String[] args) {

		SpringApplication.run(AmsRemedyAutomationApplication.class);

	}

	@Override
	public void run(String... args) throws Exception {

		jobRunner.runBatchJob();
		
			
	}


}
