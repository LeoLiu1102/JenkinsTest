package AddClicks;

import java.util.ArrayList;
import java.util.List;

public class AddClicks2 {
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		List<String> csvFeed = new ArrayList<String>();
		String filePath = "./linklist.csv";	
		ReadCSV rd = new ReadCSV(filePath, csvFeed);
		
		List<Campaign> campaign = new ArrayList<Campaign>();
		for(int i = 0; i < csvFeed.size(); i++){			
			campaign.add(new Campaign(csvFeed.get(i)));			
		}
		
		//adding clicks and unique clicks
		for(Campaign camp:campaign){
			
			int restClick = camp.getClick(); // update Click number dynamically 
			
			//unique clicks
			for(int i = 0; i < camp.getUclick(); i++){
				camp.addClick().clicks();
				camp.addCookieList();
				restClick--;
			}
			
			//clicks
			for(int j = 0; j < restClick; j++){
				camp.addClick().clicksWithCookie(camp.getCookie());
			}
			
			//leads
			for(int l = 0; l < camp.getLead(); l++){
				camp.addClick().s2sLead(camp.getLeadHash(), camp.getCookieID());
			}
		}
		
		//rd.showMessage("Yeh! Yeh! Yeh!\n" + "It's done!!!");
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total time used: " + (endTime-startTime)/1000 + " Seconds");
		System.out.println("Test Done!");
	}
}
