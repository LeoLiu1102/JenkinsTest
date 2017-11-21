package AddClicks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.client.CookieStore;

public class Campaign {
	
	private String testLink;
	private int numOfClick;
	private int numOfUclick;
	private int numOfLead;
	private String leadHash;
	
	private ClickAndLead c;
	private List<CookieStore> cookieList = new ArrayList<CookieStore>();
	private List<String> cookieIDList = new ArrayList<String>();
	
	Campaign(String campInfo){
		
		String[] temp = campInfo.split(",");
		
		setLink(temp[0].trim());
		setClick(Integer.parseInt(temp[1].trim()));
		setUclick(Integer.parseInt(temp[2].trim()));
		try{
			setLead(Integer.parseInt(temp[3].trim()));
			setLeadHash(temp[4].trim());
		}catch(Exception e){
			numOfLead = 0;
		}
		
		c = new ClickAndLead(testLink);
	}
	
	private void setLink(String testLink){
		this.testLink = testLink;
	}
	
	public String getLink(){
		return testLink;
	}
	
	private void setClick(int numOfClick){
		this.numOfClick = numOfClick;
	}
	
	public int getClick(){
		return numOfClick;
	}
	
	private void setUclick(int uClick){
		this.numOfUclick = uClick;
	}
	
	public int getUclick(){
		return numOfUclick;
	}
	
	private void setLead(int numOfLead){
		this.numOfLead = numOfLead;
	}
	
	public int getLead(){
		return numOfLead;
	}
	
	private void setLeadHash(String leadHash){
		this.leadHash = leadHash;
	}
	
	public String getLeadHash(){
		return leadHash;
	}
	
	public ClickAndLead addClick(){
		return c;
	}
	
	public void addCookieList(){
		cookieList.add(c.getCookieStore());
		cookieIDList.add(c.getCookieID());
	}
	
	public CookieStore getCookie(){
		//randomly pick a cookie to add clicks
		Random rand = new Random();
		CookieStore randomCookie = cookieList.get(rand.nextInt(cookieList.size()));
		return randomCookie;
	}
	
	public String getCookieID(){
		//randomly pick a cookie to add clicks
		Random rand = new Random();
		int n = rand.nextInt(cookieIDList.size());
		String randomCookieID = cookieIDList.get(n);
		cookieIDList.remove(n);
		return randomCookieID;
	}

}
