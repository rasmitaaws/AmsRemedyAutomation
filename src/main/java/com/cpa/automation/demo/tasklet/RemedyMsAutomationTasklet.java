package com.cpa.automation.demo.tasklet;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cpa.automation.demo.ms.MsTeamController;
import com.cpa.automation.demo.util.HttpClientHelper;

@Component
public class RemedyMsAutomationTasklet  implements Tasklet {

	@Autowired
	MsTeamController msTeamController;
	
	@Autowired
	HttpClientHelper httpClientHelper;
	
	
	 JobExecution jobExecution;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		//String code =httpClientHelper.getAuthorizationCode();
		jobExecution=chunkContext.getStepContext().getStepExecution().getJobExecution();
		Date lastUpdated =jobExecution.getLastUpdated();
		msTeamController.getChatDetailsPage(lastUpdated);
		
		return RepeatStatus.FINISHED;
	}

}
