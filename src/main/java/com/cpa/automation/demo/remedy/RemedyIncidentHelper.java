package com.cpa.automation.demo.remedy;

public class RemedyIncidentHelper {
	
	
	public static  String updateIncident(String msg)
	{
		String baseURL ="http://vtrvitstp-03:8008"; //sc.next();
		//	System.out.println("Enter user name: ");
			String userName = "rasmita.jena";
		//	System.out.println("Enter password: ");
			String password = "remedy";
		
		
			String token= BMCRestTestController.login(baseURL, userName, password);
			
			System.out.println("*************** Getting JWT Login *****************");
			System.out.println("Login result :"+token);
			System.out.println();
			
			
			System.out.println("*************** Updating  Incident INC000000003004 *****************");
		    BMCRestTestController.updateIncident(token.trim(),msg);
		    
		    
		    return "update";
		    
		    
	}

}
