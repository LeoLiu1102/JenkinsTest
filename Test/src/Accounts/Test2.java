package Accounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Test2 extends Functions{
	
	private String campID;
	private String link;
	private String payinType;
	private String pixelType;
	private String countryCode;
	private String country;
	private String cookie;
	private String server;
	private String testCampID;
	private String testCreativeID;
	private String testAffiliateID;
	
	
	Test2(WebDriver driver, String id) throws InterruptedException{
		
		this.campID = id;
		this.link = GetLink(driver, id);
		this.payinType = GetPayinType(driver);
		this.pixelType = GetPixelType(driver);
		this.countryCode = GetCountryandCode(driver,id).get(0);
		this.country = GetCountryandCode(driver,id).get(1);
		
	}
	
	Test2(String cookie, String server, String testCampID, String testCreativeID, String testAffiliateID){
		
		this.cookie = cookie;
		this.server = server;
		this.testCampID = testCampID;
		this.testCreativeID = testCreativeID;
		this.testAffiliateID = testAffiliateID;

		
	}
	
	
	public static void main (String []arg) throws Exception{
		
		WebDriver driver = new FirefoxDriver();
		Functions test = new Functions();
		
		test.Login(driver);
		
		List<String> campID = test.GetCamIdFromFile();
		Test2 [] campaign = new Test2[campID.size()]; 
		
		for(int i = 0; i < campID.size(); i++){
			
			campaign[i] = new Test2(driver, campID.get(i));
			
		}
		
		driver.quit();
		
		//Test US campaigns
		int count = 1;
		for(int i = 0; i < campID.size(); i++){
						
			if(campaign[i].countryCode.equals("US")){
				
				driver = new FirefoxDriver();
				
				test.pixelTestTool(driver, campaign[i].pixelType);				
				
				if(count == 1){
					
					test.ChangeProxy(campaign[i].country);
				
				}
				count++;
				
				test.linkTest(driver, campaign[i].link);
				//campaign[i] = Test2(test.GetCookies(driver), test.GetServer(driver, campaign[i].link), test.getTestCampaignID(driver), test.getTestCreativeID(driver), test.getTestAffID(driver));

			}
			
			driver.quit();

		}
		
		for(int i = 0; i < campID.size(); i++){
			System.out.println(campaign[i].campID);
			System.out.println(campaign[i].link);
			System.out.println(campaign[i].payinType);
			System.out.println(campaign[i].pixelType);
			System.out.println(campaign[i].country);
			System.out.println(campaign[i].server);
			System.out.println(campaign[i].testCampID);
			System.out.println(campaign[i].testCreativeID);
			System.out.println(campaign[i].testAffiliateID);
		}
	}
}

		
	
	

