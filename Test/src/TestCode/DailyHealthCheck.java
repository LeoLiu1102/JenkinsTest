package TestCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

/**
* DailyHealthCheck.java is for the campaigns daily health check
* 
* @author  Leo Liu
* @version 1.0
* @since   2016-06-01 
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
		//WebDriver driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", filePath+"chromedriver.exe");

		driver = new ChromeDriver();
		
		Campaign camp = new Campaign(driver, user, filePath);

		List<Campaign> campaignList = new ArrayList<Campaign>();

		//Collect campaign informations
		for(int i = 0; i < camp.getSize(); i++){

			campaignList.add(new Campaign(i));
			
		}
		
		List<Campaign> sortedList = campaignList;
				
		Collections.sort(sortedList, camp.new comp1());
		
		String previousCountry = null;
		
		WebDriver driver2 = null;
		for(int i = 0; i < sortedList.size(); i++){
			
			String mainWindow=null;
			while(true){

				ProfilesIni profile = new ProfilesIni();				 
				FirefoxProfile myProfile = profile.getProfile("Test");
				driver2 = new FirefoxDriver(myProfile);
				camp.setDriver(driver2);
				
				try{
					camp.pixelTestTool(driver2, sortedList.get(i).getPixel(),sortedList.get(i).getCountry());
					
					//Switch to new window
					mainWindow = camp.SwitchToPopupWindow();
					
					camp.linkTest(sortedList.get(i).getLink());
					
					sortedList.get(i).setTestInfo();
					break;
				}catch(Exception e){
					
					System.out.println("Test campaign " + sortedList.get(i).getID() + " time(s) failed");	

				}
							
			}

			
			//Fire the Pixel
			if(sortedList.get(i).getTestCampaignID().equals(sortedList.get(i).getID()) //campaign ID same
					&& (!sortedList.get(i).getTestCreativeID().equals("0")) //Creative ID is not equals 0
						&& (sortedList.get(i).getTestAffiliateID().equals("5"))) //Affiliate ID equals 5
			{
				camp.fire(sortedList.get(i).getPayin(), sortedList.get(i).getPixel());
				sortedList.get(i).setRedirectStatus(false);
				System.out.println(sortedList.get(i).getID() + " pixel fired!");
			}
			else{
				sortedList.get(i).setRedirectStatus(true);
			}
			camp.SwitchToMainWindow(mainWindow);
			
			boolean disconnect = false;
			while(!disconnect){
				try{
					 //click Disconnect button
					 camp.waitForElementByXpath(20, ".//*[@id='buttons']/input");
					 driver2.findElement(By.xpath(".//*[@id='buttons']/input")).click();
					 
					 //Wait disconnected then close all browser.
					 camp.waitForElementByXpath(20, ".//*[@id='middle-content']/p/span/span");
					 if(driver2.findElement(By.xpath(".//*[@id='middle-content']/p/span/span")).getText().trim().equals("Disconnected")){
						 disconnect = true;
						 driver2.quit();
					 }
				}catch(Exception e){
					System.out.println("Proxy was not disconnected, trying again...");
				}
			}
			
			sortedList.get(i).toInfo(i);
			
			//driver2.quit();;
		}
				
		Collections.sort(sortedList, camp.new comp2());
		campaignList = sortedList;
		
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
