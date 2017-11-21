package AddClicks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddClicksAndLeads {
	
	public static void main(String[] args) throws IOException {
		
		long startTime = System.currentTimeMillis();
		
		List<String> csvFeed = new ArrayList<String>();
		String filePath = "./CampInfoList.csv";	
		new ReadCSV(filePath, csvFeed);
		
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
				try {
					camp.addClick().thirdPartyLead(camp.getCookie(), "cfrm", camp.getLeadHash(), "88982");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Processing time: " + (endTime-startTime)/1000 + " Seconds");
		System.out.println("Test Done!");
	}
}
