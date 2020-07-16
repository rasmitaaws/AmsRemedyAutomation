
package com.cpa.automation.demo.job;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class TeamRemedyAutomationJob {

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	
//private DataSource dataSource;
	
	
	@Autowired
	@StepScope
	@Qualifier(value = "remedyMsAutomationTasklet")
	private Tasklet remedyMsAutomationTasklet;
	

	
	/*
	 * @Qualifier(value = "dataSource")
	 * 
	 * @Bean DataSource dataSource(Environment env) { HikariConfig dataSourceConfig
	 * = new HikariConfig();
	 * 
	 * dataSourceConfig.setDriverClassName(env.getRequiredProperty(
	 * "spring.datasource.driver-class-name"));
	 * dataSourceConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"))
	 * ; dataSourceConfig.setUsername(env.getRequiredProperty(
	 * "spring.datasource.username"));
	 * dataSourceConfig.setPassword(env.getRequiredProperty(
	 * "spring.datasource.password"));
	 * 
	 * return new HikariDataSource(dataSourceConfig); }
	 */

	
	@Autowired
	public DataSource dataSource(){
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("org.postgresql.Driver");
	    dataSource.setUrl("jdbc:postgresql://localhost:5432/automation");
	    dataSource.setUsername( "postgres" );
	    dataSource.setPassword( "postgres" );
	    return dataSource;
	}
	
	@Autowired
	public TeamRemedyAutomationJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		
	}

	@Qualifier(value = "teamRemedyAutomationJob")
	@Bean
	public Job teamRemedyAutomationJobObj() throws Exception {
		return this.jobBuilderFactory.get("teamRemedyAutomationJob").start(step1TeamRemedyAutomationJob()).build();
	}

	@Qualifier(value = "teamRemedyAutomationJob")
	@Bean
	public Step step1TeamRemedyAutomationJob() throws Exception {
		return this.stepBuilderFactory.get("step1TeamRemedyAutomationJob").tasklet(remedyMsAutomationTasklet).build();
	}

}
