package com.cpa.automation.demo.remedy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cpa.automation.demo.util.OAuthConstants;

@Component
public class RemedyIncidentHelper {
	
	public RemedyIncidentHelper() {
		
	}
	
	@Autowired
	BMCRestTestController bMCRestTestController;
	
	/** process Folder **/
	@Value("${remedy.username}")
	private String userName;
	
	
	/** process Folder **/
	@Value("${remedy.password}")
	private String  password;
	
	
	
	public   String updateIncident(String msg)
	{
		String baseURL ="http://vtrvitstp-03:8008"; //sc.next();
			
		
			String token= bMCRestTestController.login(baseURL, userName, password);
			
			System.out.println("*************** Getting JWT Login *****************");
			System.out.println("Login result :"+token);
			System.out.println();
			
			
			System.out.println("*************** Updating  Incident INC000000003004 *****************");
			bMCRestTestController.updateIncident(token.trim(),msg);
		    
		    
		    return "update";
		    
		    
	}

}
