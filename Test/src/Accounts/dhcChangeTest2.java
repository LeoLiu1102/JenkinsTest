package Accounts;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class dhcChangeTest2 {
	
  private WebDriver driver;
  final String affiliateLinkText = "CD5 - Angelo Pollastrone - Mundomedia Pvt Ltd";
  final String URL = "www.mm-tracking.com";
  //final String URL = "qa-dev.dev-mt01";
 
  public static void  main (String []arg) throws Exception{
	  
	  
	  dhcChangeTest2 obj = new dhcChangeTest2();
	  obj.driver = new FirefoxDriver(); 

	  List<String> camID = new ArrayList<String>();
	  List<String> serverName = new ArrayList<String>();
	  
	  //IDs displayed in Fire Pixel page
	  List<String> testCampaignID = new ArrayList<String>();
	  List<String> testAffID = new ArrayList<String>();
	  
	  List<String> cookies = new ArrayList<String>();
	  
	  List<String> pixelType = new ArrayList<String>();
	  List<String> payinType = new ArrayList<String>();
	  //List<String> capStatus = new ArrayList<String>();
	  List<String> testCountry = new ArrayList<String>();
	  List<String> countryCode = new ArrayList<String>();
	  
	  List<String> link = new ArrayList<String>();
	  
	  String date = String.format("%td-%<tb-%<ty", new Date());	  
	  
	  obj.Login();
	  obj.GetCamIDs(camID);
	  for(int count=0; count < camID.size(); count++){
		  link.add(obj.GetLink(camID.get(count)));
		  pixelType.add(obj.GetPixelType());
		  payinType.add(obj.GetPayinType());
		  obj.GetTestCountry(testCountry, countryCode);	
	  }

	  obj.driver.close();

	  //campaign redirecting status
	  List<String> status = new ArrayList<String>();
	  
	  ProfilesIni profile = new ProfilesIni();				 
	  FirefoxProfile myProfile = profile.getProfile("Test");
	  for(int count = 0; count < camID.size(); count++){
		  
		  obj.driver = new FirefoxDriver(myProfile);
		  
		  try{
		  obj.LinkTesting(link.get(count), pixelType.get(count), testCountry, testCampaignID, testAffID, count);
			
		  cookies.add(obj.GetCookies());
		  serverName.add(obj.GetServer(link.get(count)));
		  
		  if(testCampaignID.get(count) == null || testCampaignID.get(count).isEmpty()){
			  
			  System.err.println("Tracking Issue!! Please check the MT system!!");
			  
		  }
		  
		  if(camID.get(count).equals(testCampaignID.get(count)) && testAffID.get(count).equals("5")){
			  obj.Fire(payinType.get(count), pixelType.get(count));
			  status.add("Not-Redirecting");
		  }  
		  else{
			  status.add("Redirecting");
		  }
		  
		  
		  obj.driver.quit();
		  }
		  catch(Exception e){
			  System.err.println("Campaign " + testCampaignID.get(count) + " test failed!!!");
		  }
	  }
	  
	  obj.CloseProxy();
	  
	  obj.driver = new FirefoxDriver();
	  obj.Login();	 
	  
	  
	  File file = new File("C:/Users/qauser/Desktop/Output.xlsx");
	  
	  XSSFWorkbook resultBook = new XSSFWorkbook();
	  XSSFSheet sheet = resultBook.createSheet("DHC");
	  
	  for(int count = 0; count < camID.size(); count++){
		  
		  int leadRow = obj.CheckLead(cookies.get(count), camID.get(count));
		  
		  XSSFRow newRow = sheet.createRow(count);
		  XSSFCell cell0 = newRow.createCell(0);
		  XSSFCell cell1 = newRow.createCell(1);
		  XSSFCell cell2 = newRow.createCell(2);
		  XSSFCell cell3 = newRow.createCell(3);
		  XSSFCell cell4 = newRow.createCell(4);
		  XSSFCell cell5 = newRow.createCell(5);
		  XSSFCell cell6 = newRow.createCell(6);
		  XSSFCell cell7 = newRow.createCell(7);
		  XSSFCell cell8 = newRow.createCell(8);
		  XSSFCell cell9 = newRow.createCell(9);
		  XSSFCell cell10 = newRow.createCell(10);
		  
		  if(leadRow > 0){
				
			  //System.out.println("Campaign "+camID.get(count)+" Lead Tracked. Test PASS!");	
			  cell0.setCellValue(date);
			  cell1.setCellValue(testAffID.get(count));
			  cell2.setCellValue(camID.get(count));
			  cell3.setCellValue(obj.getCreativeID(leadRow));
			  cell4.setCellValue(countryCode.get(count) + "/" + obj.GetIP(leadRow));
			  cell5.setCellValue("FF");
			  cell6.setCellValue(pixelType.get(count));
			  cell7.setCellValue("PASS");
			  cell8.setCellValue("Symantec Endpoint Protection");
			  cell9.setCellValue(serverName.get(count));
			  cell10.setCellValue("");
		  				
		  }
		  else{
			  if(status.get(count).equals("Not-Redirecting")){
				  
				  System.err.println("Campaign "+camID.get(count)+" No lead Tracked. Test fail!");			
				  
				  
				  cell0.setCellValue(date);
				  cell1.setCellValue(testAffID.get(count));
				  cell2.setCellValue(camID.get(count));
				  cell3.setCellValue("");
				  cell4.setCellValue(countryCode.get(count));
				  cell5.setCellValue("FF");
				  cell6.setCellValue(pixelType.get(count));
				  cell7.setCellValue("FAIL");
				  cell8.setCellValue("Symantec Endpoint Protection");
				  cell9.setCellValue(serverName.get(count));
				  cell10.setCellValue("NO LEAD TRACKED!!");
				  
			  }
			  else{
				  
				  if(obj.getCapInfo(camID.get(count)).equals("red")){
				  System.err.println("Campaign "+camID.get(count)+" Daily lead cap reached. It is redirecting to Campaign "+testCampaignID.get(count));
				  
				  cell0.setCellValue(date);
				  cell1.setCellValue(testAffID.get(count));
				  cell2.setCellValue(camID.get(count));
				  cell3.setCellValue("");
				  cell4.setCellValue(countryCode.get(count));
				  cell5.setCellValue("FF");
				  cell6.setCellValue(pixelType.get(count));
				  cell7.setCellValue("PASS");
				  cell8.setCellValue("Symantec Endpoint Protection");
				  cell9.setCellValue(serverName.get(count));
				  cell10.setCellValue("Daily lead cap reached. Campaign redirecting");
				  }
				  
				  
				  else{
					  
					  System.err.println("Campaign "+camID.get(count)+" is redirecting to Campaign "+testCampaignID.get(count));
					  
					  cell0.setCellValue(date);
					  cell1.setCellValue(testAffID.get(count));
					  cell2.setCellValue(camID.get(count));
					  cell3.setCellValue("");
					  cell4.setCellValue(countryCode.get(count));
					  cell5.setCellValue("FF");
					  cell6.setCellValue(pixelType.get(count));
					  cell7.setCellValue("PASS");
					  cell8.setCellValue("Symantec Endpoint Protection");
					  cell9.setCellValue(serverName.get(count));
					  cell10.setCellValue("Campaign "+camID.get(count)+" is redirecting to Campaign "+testCampaignID.get(count));				  
				  }
			  }
		  }
		  
		  FileOutputStream out = new FileOutputStream(file);
		  resultBook.write(out);
		  out.close();
	  }
	  resultBook.close();
	  
	  obj.tearDown();	
	  
  }

  public void Login() {
	  
	  String baseUrl = "http://"+URL+"/admin/login";
	  
	  driver.get(baseUrl);
	  driver.manage().window().maximize();
	  driver.findElement(By.id("username")).clear();
	  driver.findElement(By.id("username")).sendKeys("leo.l");
	  driver.findElement(By.id("password")).clear();
	  driver.findElement(By.id("password")).sendKeys("Kate1205");
	  driver.findElement(By.id("login")).click();
	  String selectLoginXpath = "html/body/div[2]/div/div/div[2]/table/tbody/tr/td[1]/a";
	  waitForElementByXpath(30, selectLoginXpath);
	  driver.findElement(By.xpath(selectLoginXpath)).click();
	  String statsTabXpath = ".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]";
	  waitForElementByXpath(30, statsTabXpath);
    
  }
  
  public void GetCamIDs(List<String> camID){
	  
	  int numOfClicks;
	  int numOfLeads;
	  
	  String statsTableXpath = ".//*[@id='stats_table']";
	  driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]")).click();
	  driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li[1]/ul/li[1]/a")).click();
	  waitForElementByXpath(30, statsTableXpath);	  
	  
	  int count = 0;
	  int row = 3;
	  
	  while(count < 10){
		  
		  //get numbers of clicks and leads, remove the comma(,), then convert it to integer
		  numOfClicks = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[12]")).getText().replaceAll(",", ""));
		  numOfLeads = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[14]/span")).getText().replaceAll(",", ""));
		  
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
	  
  }
  
  public String GetLink(String campaignID){
	  
	  String camUrl = "http://"+URL+"/admin/details/campaign/c/";
	  
	  driver.navigate().to(camUrl+campaignID);
	  
	  //check if the the "Regular" radio button exists
	  String testLinkXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[8]/td[2]/a";
	  waitForElementByXpath(20,testLinkXpath);
	  String link = driver.findElement(By.xpath(testLinkXpath)).getAttribute("href").replaceAll("&.+", "");
	  
	  return link;
  }
  
  public String GetPixelType(){
	  String pixelTypeXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[12]/td[1]";
	  waitForElementByXpath(20, pixelTypeXpath);
	  
	  String pixelType;
	  
	  if(driver.findElement(By.xpath(pixelTypeXpath)).getText().trim().equals("Image Pixel:"))
		  pixelType = "Regular";
	  else
		  pixelType = "S2S";
	  
	  return pixelType;
  }

  public String GetPayinType(){
	  
	  String payinTypeXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[4]/td[2]";
	  waitForElementByXpath(20, payinTypeXpath);
	  
	  //get Pay in Type, trim the string
	 String payinType;
	 if(driver.findElement(By.xpath(payinTypeXpath)).getText().trim().equals("Variable"))
		 payinType = "Variable";
	 else
		 payinType = "Fixed";
	 
	 return payinType; 
  }
  
  public String getCapInfo(String campaignID){
	  
	  String camUrl = "http://"+URL+"/admin/details/campaign/c/";
	  
	  driver.navigate().to(camUrl+campaignID);
	  
	  try{
	  String dailyCapXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[17]/td[2]/table/tbody/tr/td[3]/span";
	  waitForElementByXpath(20, dailyCapXpath);
	  String capStatus = driver.findElement(By.xpath(dailyCapXpath)).getAttribute("class");
	  
	  return capStatus;
	  }catch(Exception e){
		  return "";
	  }
  }
  
  public void GetTestCountry(List<String> testCountry, List<String> countryCode) throws InterruptedException{
	  //click Details tab in pop up window
	  String detailsTabXpath = ".//*[@id='mundoTabs']/ul/li[2]/a";
	  waitForElementByXpath(20,detailsTabXpath);
	  driver.findElement(By.xpath(detailsTabXpath)).click();
	  Thread.sleep(2000);
	  
	  //Country List radio button selected. If there are multiple countries, randomly pick 1 from the list
	  String geoRadioBtn2Xpath = ".//*[@id='DATA-geotracking-1']";
	  waitForElementByXpath(20,geoRadioBtn2Xpath);

	  
	  if(driver.findElement(By.xpath(geoRadioBtn2Xpath)).isSelected()) {
		  
	    	WebElement countryTable = driver.findElement(By.id("geotracking_countries_selected"));
	    	
	    	List<WebElement> countryList = countryTable.findElements(By.tagName("option"));
	    	
	    	List<String> country = new ArrayList<String>();
	    	
	    	for(WebElement ele:countryList){
	    		try{
	    		country.add(ele.getAttribute("value"));
	    		}catch(Exception e){
	    			System.out.println(driver.findElement(By.xpath("html/body/h1")).getText());
	    			System.err.println("Country can not be added");
		    		testCountry.add("United States");
		    		countryCode.add("US");
	    		}
	    	}
	    	if(country.size()>1){
	    		if(country.contains("US")){
		    		testCountry.add("United States");
		    		countryCode.add("US");
	    		}
	    		else if(country.contains("FR")){
		    		testCountry.add("France");
		    		countryCode.add("FR");
	    		}
	    		else if(country.contains("GB")){
		    		testCountry.add("United Kingdom");
		    		countryCode.add("GB");
	    		}
	    		else{	    		
	    			Random rand = new Random();
	    			int  n = rand.nextInt(country.size());
	    			countryCode.add(country.get(n));
	    			
	    			//Locale: find full country name by iso country code
	    			Locale loc = new Locale("", country.get(n));
	    			testCountry.add(loc.getDisplayCountry());

	    		}
	    	}

	    	else{
	    		countryCode.add(country.get(0));
	    			
	    		//Locale: find full country name by iso country code
	    		Locale loc = new Locale("", country.get(0));
	    		testCountry.add(loc.getDisplayCountry());
	    	}
	    }
	  
	    else{
	    	testCountry.add("United States");
	    	countryCode.add("US");
	    }
  
  }
  
  public void LinkTesting(String testLink, String pixelType, List<String> testCountry, List<String> testCampaignID, List<String> testAffID, int count){
	  
	String firePixelURL;	
	//checking pixel type
	if(pixelType.equals("Regular")){		
		firePixelURL = "http://"+URL+"/tools/p_test.php";
	}

	else{		
		firePixelURL = "http://"+URL+"/tools/s2s_test.php";
	}
	
	//Open the Pixel Test page
	driver.get(firePixelURL);
	
	 //Select target country
	 waitForElementByXpath(20, ".//*[@id='locationsform']/p/select");
	 Select drpCountry = new Select(driver.findElement(By.xpath(".//*[@id='locationsform']/p/select")));
	 drpCountry.selectByVisibleText(testCountry.get(count));
	 
	 //Click the target link
	 waitForElementByXpath(20, ".//*[@id='middle-content']/p[2]/a");
	 driver.findElement(By.xpath(".//*[@id='middle-content']/p[2]/a")).click();
	 
	 //Switch to new window
	 String mainWindow = SwitchToPopupWindow();
	 
	try{
	//Clean all browser cookies
	driver.manage().deleteAllCookies();
	GetActualIDs(testCampaignID, testAffID, testLink);
	}catch(Exception e){
		System.err.println("Link Test");
	}
  }
    
  public String GetServer(String testlink){
	  
	String tmpLink = testlink.replaceAll("mt/.+", "");
	
	OpenNewTab();
	driver.get(tmpLink+"info.php");
	  
	String serverNameXpath = "html/body";
	waitForElementByXpath(30,serverNameXpath);
	String tempName = driver.findElement(By.xpath(serverNameXpath)).getText();
	CloseTab();
	
	//Capitalize the first letter and return
	return tempName.substring(0,1).toUpperCase() + tempName.substring(1);
  } 
  
  //running inside LinkTesting() method
  public void GetActualIDs(List<String> testCampaignID, List<String> testAffID, String testLink){
		
	String testCampaignIDXpath = "html/body/table/tbody/tr[1]/td[2]";
	String testAffiliateIDXpath = "html/body/table/tbody/tr[3]/td[2]";
	
	try{
	waitForElementByID(60, "test_link");
	//Enter the test link
	driver.findElement(By.id("test_link")).sendKeys(testLink);
	//Click "Start Test" button
	driver.findElement(By.id("start")).click();
	
	//check if leaving the landing page trigger the popup confirmation, if yes, confirm leaving to close the window.
	String parentHandle;
	parentHandle = SwitchToPopupWindow();
	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	
	if(isAlertPresent())
		driver.switchTo().alert().accept();
	
	SwitchToMainWindow(parentHandle);
	
	//get IDs in pixel Test page
	waitForElementByXpath(180, testCampaignIDXpath);
	
	if(driver.findElement(By.xpath(testCampaignIDXpath)).getText() == null || driver.findElement(By.xpath(testCampaignIDXpath)).getText().isEmpty()){
		  
		Thread.sleep(5000);
		driver.navigate().refresh();
		  
	}
	
	testCampaignID.add(driver.findElement(By.xpath(testCampaignIDXpath)).getText());
	waitForElementByXpath(180, testAffiliateIDXpath);
	testAffID.add(driver.findElement(By.xpath(testAffiliateIDXpath)).getText());
	
	}catch(Exception e){
		System.err.println("Link Test");
	}
  }

  public void ChangeProxy(String testCountry){
	  
	  JOptionPane.showMessageDialog(null, "<html><body><p>Please change the Proxy server to <h2>"+ testCountry +"</h2></P></body></html>");

  }
  
  public void CloseProxy(){
	  
	  JOptionPane.showMessageDialog(null, "Please disconnect the VPN");

  }

  public void Fire(String payinType, String pixelType) {
	  
	  String parentHandle;
	  
	  if(pixelType.equals("Regular")){
		  String btnFirePixel = "html/body/form[1]/input";
		  driver.findElement(By.xpath(btnFirePixel)).click();
		  
		  parentHandle = SwitchToPopupWindow();
		  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		  SwitchToMainWindow(parentHandle);

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
		  
		  parentHandle = SwitchToPopupWindow();
		  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		  SwitchToMainWindow(parentHandle);			  
			
	}//else end		  

  }
  
  public String GetCookies(){
	  
	WebDriverWait wait = new WebDriverWait(driver, 60);
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/table/tbody/tr[3]/td[2]")));
	String cookies = driver.manage().getCookieNamed("mt_lds").getValue();
	
	return cookies;
  }
  
  public int CheckLead(String cookies, String campaignID) throws InterruptedException{	  

	  String leadRptURL = "http://"+URL+"/admin/stats/leadreport";
	  driver.navigate().to(leadRptURL);
	  
	  String SearchCidXpath = ".//*[@id='search_campaign']";
	  waitForElementByXpath(30, SearchCidXpath);
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
	  waitForElementByXpath(30, statsTableXpath);
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
  
  public String GetIP(int leadRow){
	  return driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+leadRow+"]/td[5]")).getText();	
  }
  
  public String getCreativeID(int leadRow){
	  return driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+leadRow+"]/td[4]")).getText();
  }
  
  
  public void waitForElementByXpath(int waitSecond, String Xpath){
	  
	  WebDriverWait wait = new WebDriverWait(driver, waitSecond);
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	  
  }


  public void waitForElementByID(int waitSecond, String ID){
	  
	  WebDriverWait wait = new WebDriverWait(driver, waitSecond);
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	  
  }

  public void OpenNewTab(){
	driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
  }
  
  public void CloseTab(){
	driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
	driver.switchTo().defaultContent();
  }
  
  public String SwitchToPopupWindow(){
	  
	  //Switch to newly opened window
	  String parentHandle = driver.getWindowHandle();
	  
	  //get the current window handle
	  for (String winHandle : driver.getWindowHandles()) {
	    	
		  // switch focus of WebDriver to newly opened window)
		  driver.switchTo().window(winHandle); 
		  
	  } //for loop end
	  
	  return parentHandle;
  }
  public void SwitchToMainWindow(String parentHandle){
	  
	  driver.switchTo().window(parentHandle);
	  
  }
  
  public boolean isAlertPresent() 
  { 
      try 
      { 
          driver.switchTo().alert(); 
          return true; 
      }   
      catch (NoAlertPresentException Ex) 
      { 
          return false; 
      }    
  }   

 
  public void tearDown() throws Exception {
	  
	  while(driver != null){
		  driver.quit();
		  System.exit(1);
	  }
  }  

}
