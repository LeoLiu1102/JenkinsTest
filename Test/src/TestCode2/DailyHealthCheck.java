package TestCode2;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
* DailyHealthCheck.java is for the campaigns daily health check, no changing proxy needed.
* 
* @author  Leo Liu
* @version 2.0
* @since   2017-01-06 
*/

public class DailyHealthCheck{
	
	static userLogin user;
	final static String filePath = "C:/Users/qauser/Desktop/Files/";
	
	public static void main(String[] args) {
		user = new userLogin(filePath);
		user.createFrame();
	}

	public void startTest(){
		WebDriver driver;

/*		System.setProperty("webdriver.gecko.driver",filePath+"geckodriver.exe");
		DesiredCapabilities cap = DesiredCapabilities.firefox();
		cap.setCapability("marionette", true);
		driver = new FirefoxDriver();*/
		
		System.setProperty("webdriver.chrome.driver", filePath+"chromedriver.exe");

		driver = new ChromeDriver();
		
		Campaign camp = new Campaign(driver, user, filePath);

		List<Campaign> campaignList = new ArrayList<Campaign>();

		//Collect campaign informations
		for(int i = 0; i < camp.getSize(); i++){

			campaignList.add(new Campaign(i));
			
		}

		System.setProperty("webdriver.gecko.driver",filePath+"geckodriver.exe");
		DesiredCapabilities cap = DesiredCapabilities.firefox();
		cap.setCapability("marionette", true);
		WebDriver driver2 = null;
		
		for(int i = 0; i < campaignList.size(); i++){
			
			int count = 1;
			String mainWindow=null;
			boolean link_test = true;
			while(link_test && count <=3){

				if(camp.getbaseURL().contains("qa-dev")){
					driver2 = new FirefoxDriver();
				}
				else{
					ProfilesIni profile = new ProfilesIni();
					FirefoxProfile myProfile = profile.getProfile("Test");
					driver2 = new FirefoxDriver(myProfile);
				}

				camp.setDriver(driver2);
				
				try{
					camp.pixelTestTool(driver2, campaignList.get(i).getPixel(),campaignList.get(i).getCountry());
					
					if(camp.getbaseURL().contains("mm-tracking.com")){
						//Switch to new window
						mainWindow = camp.SwitchToPopupWindow();
					}
					
					camp.linkTest(campaignList.get(i).getLink());
					
					campaignList.get(i).setTestInfo();
					link_test = false;
				}catch(Exception e){
					
					System.out.println("Test campaign " + campaignList.get(i).getID() + " " + count + " time(s) failed");
					
					if(camp.getbaseURL().contains("mm-tracking.com")){
						//Switch to new window
						camp.SwitchToMainWindow(mainWindow);
						camp.disconnectProxy(driver2);
					}
					driver2.quit();

				}
				
				count++;				
			}

			
			//Fire the Pixel
			if(campaignList.get(i).getTestCampaignID().equals(campaignList.get(i).getID()) //campaign ID same
					&& (!campaignList.get(i).getTestCreativeID().equals("0")) //Creative ID is not equals 0
						&& (campaignList.get(i).getTestAffiliateID().equals("5"))) //Affiliate ID equals 5
			{
				camp.fire(campaignList.get(i).getPayin(), campaignList.get(i).getPixel());
				campaignList.get(i).setRedirectStatus(false);
				System.out.println(campaignList.get(i).getID() + " pixel fired!");
			}
			else{
				campaignList.get(i).setRedirectStatus(true);
			}
			if(camp.getbaseURL().contains("mm-tracking.com")){
				//Switch to new window
				camp.SwitchToMainWindow(mainWindow);
				camp.disconnectProxy(driver2);
			}
			
			campaignList.get(i).toInfo(i);
			driver2.quit();
		}
		
		camp.setDriver(driver);
		for(int i = 0; i < campaignList.size(); i++){
			
			if(campaignList.get(i).getRedirectStatus()){
				campaignList.get(i).setResults("PASS"); 
				campaignList.get(i).toResultRedirecting(i);
				
			}
			else{
				boolean lead = campaignList.get(i).checkLead(campaignList.get(i).getCookie(), campaignList.get(i).getID());
				
				if(lead){
					campaignList.get(i).setResults("PASS");
					campaignList.get(i).toResultWithLead(i);
				}
				else{
					campaignList.get(i).setResults("FAIL");
					campaignList.get(i).toResultNoLead(i);
				}
			}
			
			
		}
		camp.tearDown();
			
	}

}
