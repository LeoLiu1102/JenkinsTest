package Accounts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Functions {
	
	int count = 1;
	final String affiliateLinkText = "CD5 - Angelo Pollastrone - Mundomedia Pvt Ltd";
	final String URL = "http://www.mm-tracking.com";
	//final String URL = "http://qa-dev.dev-mt01";
	
	final String baseUrl = URL+"/admin/login";
	final String camUrl = URL+"/admin/details/campaign/c/";
	
	final String regularPixelURL = URL+"/tools/p_test.php";
	final String s2sPixelURL = URL+"/tools/s2s_test.php";
	
	final String leadRptURL = URL+"/admin/stats/leadreport";
	final String errorLogUrl = URL+"/admin/lists/error-log";
	
	public void Login(WebDriver driver) {
		 
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("leo.l");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("Kate1206");
		driver.findElement(By.id("login")).click();
		String selectLoginXpath = "html/body/div[2]/div/div/div[2]/table/tbody/tr/td[1]/a";
		waitForElementByXpath(driver, 30, selectLoginXpath);
		driver.findElement(By.xpath(selectLoginXpath)).click();
		String statsTabXpath = ".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]";
		waitForElementByXpath(driver, 30, statsTabXpath);
	  
	}
	
	public List<String> GetCamIdTop10(WebDriver driver){
		 
		List<String> camID = new ArrayList<String>();
		int numOfClicks;
		int numOfLeads;
		
		String statsTableXpath = ".//*[@id='stats_table']";
		driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]")).click();
		driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li[1]/ul/li[1]/a")).click();
		waitForElementByXpath(driver, 30, statsTableXpath);	  
		
		int count = 0;
		int row = 3;
		
		while(count < 10){
		
			//get numbers of clicks and leads, remove the comma(,), then convert it to integer
			numOfClicks = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[12]")).getText().replaceAll(",", ""));
			numOfLeads = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[15]/span")).getText().replaceAll(",", ""));
			
			//get campaign IDs, skill Pay per Call type(leads < click) 
			if(numOfLeads > numOfClicks){
			
				row++; 
			}
			else{
			
				camID.add(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[3]")).getText());
				count++;
				row++;
			}
						
		}
		
		return camID;
	 
	}
	
	public List<String> GetCamIdFromFile(){
		
		List<String> camID = new ArrayList<String>();
		 
		File file = new File("C:/Users/qauser/Desktop/CampList.csv");
		try{
			
			Scanner sc = new Scanner(file);
			
			while(sc.hasNextLine()){
			
				camID.add((sc.next()));
		
			}
		
			sc.close();
		}
		catch(Exception e){
		
			System.out.println("File is not found!");
		
		}
		
		return camID;
	}
	
	public String GetLink(WebDriver driver, String campaignID){

		String link = "temp";
		
		while(link.equals("temp")){
			try {
				driver.get(camUrl+campaignID);
				
				//check if the the "Regular" radio button exists
				String testLinkXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[8]/td[2]/a";
				
				waitForElementByXpath(driver, 60,testLinkXpath);
				link = driver.findElement(By.xpath(testLinkXpath)).getAttribute("href").replaceAll("&.+", "");
			} catch (Exception e) {
				System.out.println(campaignID+" Getting link issue");
			}
		}
		
		return link;
	}
	
	public String GetPixelType(WebDriver driver){
		
		String pixelType;
		try {
			String pixelTypeXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[12]/td[1]";
			waitForElementByXpath(driver, 20, pixelTypeXpath);
			
			if(driver.findElement(By.xpath(pixelTypeXpath)).getText().trim().equals("Image Pixel:"))
			pixelType = "Regular";
			else
			pixelType = "S2S";
		} catch (Exception e) {
			System.out.println("Getting link issue");
			pixelType = "Temp pixel Type";
		}
		
		return pixelType;
	}
	
	public String GetPayinType(WebDriver driver){
	 
		String payinTypeXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[4]/td[2]";
		waitForElementByXpath(driver, 20, payinTypeXpath);
		 
		//get Pay in Type, trim the string
		String payinType;
		if(driver.findElement(By.xpath(payinTypeXpath)).getText().trim().equals("Variable"))
		 payinType = "Variable";
		else
		 payinType = "Fixed";
		
		return payinType; 
	}
	
	public List<String> GetCountryandCode(WebDriver driver, String campaignID){

		//click Details tab in pop up window
		String detailsTabXpath = ".//*[@id='mundoTabs']/ul/li[2]/a";
		waitForElementByXpath(driver, 20,detailsTabXpath);
		driver.findElement(By.xpath(detailsTabXpath)).click();
		
		//Country List radio button selected. If there are multiple countries, randomly pick 1 from the list
		String geoRadioBtn2Xpath = ".//*[@id='DATA-geotracking-1']";
		String selectedCountryXpath = ".//*[@id='geotracking_countries_selected']/option[1]";
		waitForElementByXpath(driver, 20, geoRadioBtn2Xpath);
		
	  	List<String> countryAndCode = new ArrayList<String>();
		
		if(driver.findElement(By.xpath(geoRadioBtn2Xpath)).isSelected()) {
			
			waitForElementByXpath(driver, 45, selectedCountryXpath);
		 
		  	WebElement countryTable = driver.findElement(By.id("geotracking_countries_selected"));
		  	
		  	List<WebElement> countryList = countryTable.findElements(By.tagName("option"));
		  	
		  	List<String> country = new ArrayList<String>();
		  	
		  	for(WebElement ele:countryList){

		  		country.add(ele.getAttribute("value"));

		  	}
		  	
		  	if(country.size()>1){
		  		if(country.contains("US")){
		  			countryAndCode.add("US");
		  			countryAndCode.add("United States");
		  		}
		  		else if(country.contains("FR")){
		  			countryAndCode.add("FR");
		  			countryAndCode.add("France");
		  		}
			  		else if(country.contains("GB")){
			  			countryAndCode.add("GB");
			  			countryAndCode.add("United Kingdom");
			  		}
				  		else{	    		
				  			Random rand = new Random();
				  			int  n = rand.nextInt(country.size());
				  			countryAndCode.add(country.get(n));
				  			
				  			//Locale: find full country name by iso country code
				  			Locale loc = new Locale("", country.get(n));
				  			countryAndCode.add(loc.getDisplayCountry());
				
				  		}
		  	}
		
		  	else{
		  		countryAndCode.add(country.get(0));
		  			
		  		//Locale: find full country name by iso country code
		  		Locale loc = new Locale("", country.get(0));
		  		countryAndCode.add(loc.getDisplayCountry());
		  	}
		}
		
		else{
			countryAndCode.add("US");
			countryAndCode.add("United States");

		}
		
		return countryAndCode;
	
	}
	
	
	public void pixelTestTool(WebDriver driver, String pixelType){
		  	
		//checking pixel type
		if(pixelType.equals("Regular")){
 
			//Open the Pixel Test page
			driver.get(regularPixelURL);
		}
		  
		else{		

			//Open the Pixel Test page
			driver.get(s2sPixelURL);
		}
		  	  
	}
	  
	public void linkTest(WebDriver driver, String testLink){
		
		waitForElementByID(driver, 60, "test_link");
		
		//Clean all browser cache and cookies
		driver.manage().deleteAllCookies();
		//Enter the test link
		driver.findElement(By.id("test_link")).sendKeys(testLink);
		//Click "Start Test" button
		driver.findElement(By.id("start")).click();
		  
	}
	    
	public String GetServer(WebDriver driver, String testlink){
		  
		String tmpLink = testlink.replaceAll("mt/.+", "");
		
		OpenNewTab(driver);
		driver.get(tmpLink+"info.php");
		  
		String serverNameXpath = "html/body";
		waitForElementByXpath(driver, 30,serverNameXpath);
		String tempName = driver.findElement(By.xpath(serverNameXpath)).getText();
		CloseTab(driver);
		
		//Capitalize the first letter and return
		return tempName.substring(0,1).toUpperCase() + tempName.substring(1);
	} 
	  
	//running inside LinkTesting() method
	public String getTestCampaignID(WebDriver driver) throws InterruptedException{		
		String testCampaignIDXpath = "html/body/table/tbody/tr[1]/td[2]";
	
		//get IDs in pixel Test page
		waitForElementByXpath(driver, 120, testCampaignIDXpath);
	  
		String id = driver.findElement(By.xpath(testCampaignIDXpath)).getText();
	  
		if(id == null || id.isEmpty()){
		  
			Thread.sleep(5000);
			driver.navigate().refresh();
			
			waitForElementByXpath(driver, 60, testCampaignIDXpath);  
			id = driver.findElement(By.xpath(testCampaignIDXpath)).getText();
			
			if(id.equals("") || id.isEmpty()){
				
				System.err.println("Tracking Issue!! Please check the MT system!!");
			  
			}
		  
		}
		return driver.findElement(By.xpath(testCampaignIDXpath)).getText(); 
	}
	
	public String getTestCreativeID(WebDriver driver){
		String testCreativeIDXpath = "html/body/table/tbody/tr[2]/td[2]";
		
		return  driver.findElement(By.xpath(testCreativeIDXpath)).getText();  
	}
	
	
	public String getTestAffID(WebDriver driver){
		String testAffiliateIDXpath = "html/body/table/tbody/tr[3]/td[2]";
	
		return  driver.findElement(By.xpath(testAffiliateIDXpath)).getText();  
	}
	
	public void ChangeProxy(String testCountry){
	  
		JOptionPane.showMessageDialog(null, "<html><body><p>Please change the Proxy server to <h2>"+ testCountry +"</h2></P></body></html>");
	
	}
	
	public void CloseProxy(){
	  
		JOptionPane.showMessageDialog(null, "Please disconnect the VPN");
	
	}
	
	public String checkRedirectType(WebDriver driver, String campID, String affID, String cookie){
		
		driver.navigate().to(errorLogUrl);
		String errorName = "";
		
		String searchCampID = "search_campaign";
		String searchParnerID = "search_partner";
		
		waitForElementByID(driver, 30, searchCampID);
		
		try{
			driver.findElement(By.id(searchCampID)).sendKeys(campID);
			driver.findElement(By.id(searchParnerID)).sendKeys(affID);
			driver.findElement(By.id("submit")).click();
			
			String listTableID = "list_table";
			waitForElementByID(driver, 30, listTableID);
			WebElement statsTable = driver.findElement(By.id(listTableID));
				
			List<WebElement> errorList = statsTable.findElements(By.partialLinkText(campID));
			
			if(errorList.size() > 0){
				
				for(int i=3; i < errorList.size()+3; i++){
					
					String cookieID = driver.findElement(By.xpath(".//*[@id='list_table']/table/tbody/tr["+i+"]/td[5]")).getText();
					if(cookie.contains(cookieID) && !cookieID.equals("0")){
						errorName = driver.findElement(By.xpath(".//*[@id='list_table']/table/tbody/tr["+i+"]/td[6]")).getText();
					}
				}
				
			}
			
		}
		catch(Exception e){
		
		}
		
		return errorName;
		
	}
	
	public void Fire(WebDriver driver, String payinType, String pixelType) {
		 
		String parentHandle;
		
		if(pixelType.equals("Regular")){
			String btnFirePixel = "html/body/form[1]/input";
			driver.findElement(By.xpath(btnFirePixel)).click();
			
			parentHandle = SwitchToPopupWindow(driver);
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			SwitchToMainWindow(driver, parentHandle);
			
		}//if end
			
		else{
			if(payinType.equals("Variable")){		
				//get the pixel URL and add 0.1 at the end for variable pay in type
				String pixelURL = driver.findElement(By.xpath(".//*[@id='pixel']")).getAttribute("value");
				driver.findElement(By.xpath(".//*[@id='pixel']")).clear();
				driver.findElement(By.xpath(".//*[@id='pixel']")).sendKeys(pixelURL+"0.1");				
			}
			
			//Click button to fire the pixel
			driver.findElement(By.xpath(".//*[@id='fire']")).click();
			 
			parentHandle = SwitchToPopupWindow(driver);
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			SwitchToMainWindow(driver, parentHandle);			  

		}//else end
	}
	
	public String GetCookies(WebDriver driver){
		  
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/table/tbody/tr[3]/td[2]")));
		String cookies = driver.manage().getCookieNamed("mt_lds").getValue();
		
		return cookies;
	}
	
	public int CheckLead(WebDriver driver, String cookies, String campaignID) throws InterruptedException{
		
		driver.navigate().to(leadRptURL);
				
		String SearchCidXpath = ".//*[@id='search_campaign']";
		waitForElementByXpath(driver, 30, SearchCidXpath);
		driver.findElement(By.xpath(SearchCidXpath)).clear();
		driver.findElement(By.xpath(SearchCidXpath)).sendKeys(campaignID);
		Thread.sleep(1000);
		
		String SearchPartnerXpath = ".//*[@id='search_partner']";
		driver.findElement(By.xpath(SearchPartnerXpath)).clear();
		driver.findElement(By.xpath(SearchPartnerXpath)).sendKeys("5");  
		Thread.sleep(1000);
			
		driver.findElement(By.xpath(".//*[@id='button_confirm']")).click();
		
		int row = 0;
		
		String statsTableXpath = ".//*[@id='stats_table']/table";
		waitForElementByXpath(driver, 30, statsTableXpath);
		WebElement statsTable = driver.findElement(By.xpath(statsTableXpath));
		
		try{
			List<WebElement> leadList = statsTable.findElements(By.partialLinkText(affiliateLinkText));
			
			for(int i=3; i < leadList.size()+3; i++){
				
				String cookieID = driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+i+"]/td[7]")).getText();
				if(cookies.contains(cookieID)){
					row = i;
				}
			}
			
			
		}
		catch(Exception e){
		
		}
		
		return row;
	}
	
	public String GetIP(WebDriver driver, int leadRow){
		
		return driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+leadRow+"]/td[5]")).getText();
		
	}
	
	public String streamToFile(File file, XSSFWorkbook testbook, String status) throws IOException{
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			testbook.write(out);
			out.close();
			status = "Closed";
		} 
		catch (Exception e) {
			status = "notClosed";
			JOptionPane.showMessageDialog(null, "Please close the Excel file then click OK!");
		}
		
		return status;
	}
	
	public void waitForElementByXpath(WebDriver driver, int waitSecond, String Xpath){
	 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
		
	public void waitForElementByID(WebDriver driver, int waitSecond, String ID){
	 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	 
	}
	
	public void OpenNewTab(WebDriver driver){
		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
	
	}
	
	public void CloseTab(WebDriver driver){
		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
		driver.switchTo().defaultContent();
	
	}
	
	public String SwitchToPopupWindow(WebDriver driver){
	 
		//Switch to newly opened window
		String parentHandle = driver.getWindowHandle();
		 
		//get the current window handle
		for (String winHandle : driver.getWindowHandles()) {
			   	
			// switch focus of WebDriver to newly opened window)
			driver.switchTo().window(winHandle); 
			  
		}//for loop end
		 
		return parentHandle;
	}
	
	
	public void SwitchToMainWindow(WebDriver driver, String parentHandle){
	 
		driver.switchTo().window(parentHandle);
	 
	}
	
	public boolean isAlertPresent(WebDriver driver){
		
		boolean status;
		
		WebDriverWait alertWait = new WebDriverWait(driver, 5);
	    
		try{
			if(alertWait.until(ExpectedConditions.alertIsPresent()) != null){
				status = true;
			}
			else{
				status = false;
			}
		}
		catch(Exception e){

			status = false;
		}
		
		
		return status;
	}
		
	public void tearDown(WebDriver driver) throws Exception {
		 
		while(driver != null){
			driver.quit();
			System.exit(1);
		}
	}
}