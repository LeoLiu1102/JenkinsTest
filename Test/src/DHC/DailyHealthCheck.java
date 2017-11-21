package DHC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

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
		
		System.setProperty("webdriver.gecko.driver","C:/Users/qauser/Desktop/Files/geckodriver.exe");
		DesiredCapabilities cap = DesiredCapabilities.firefox();
		cap.setCapability("marionette", true);
		WebDriver driver2;
		for(int i = 0; i < sortedList.size(); i++){
			
			int count = 1;
			while(true){
				
				driver2 = new FirefoxDriver();
				
				//driver2 = new FirefoxDriver();
				camp.setDriver(driver2);
				
				try{
					camp.pixelTestTool(driver2, sortedList.get(i).getPixel());
					if(!sortedList.get(i).getCountry().equals(previousCountry)){
						camp.changeProxy(sortedList.get(i).getCountry());
						previousCountry = sortedList.get(i).getCountry();
					}
					camp.linkTest(sortedList.get(i).getLink());
					
					sortedList.get(i).setTestInfo();
					break;
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "Get test campaign " +sortedList.get(i).getID() 
							+ " infomaiton failed. Click OK to retry " + count);
					

				}
				count++;				
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
				System.err.println(sortedList.get(i).getID() + " pixel didn't fire!");
			}
			
			sortedList.get(i).toInfo(i);
			
			driver2.quit();;
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
