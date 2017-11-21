package DHC;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Campaign{
	
	final String affiliateLinkText = "CD5 - Ange";

	final String camUrl = "admin/details/campaign/c/";	
	final String regularPixelURL = "tools/p_test.php";
	final String s2sPixelURL = "tools/s2s_test.php";
	final String leadRptURL = "admin/stats/leadreport";
	final String errorLogUrl = "admin/lists/error-log";
	final String antiVirus = "Symantec Endpoint Protection";
	String note = "";
	
	private String date = String.format("%td-%<tb-%<ty", new Date());

	private static WebDriver driver;
	private static String userId, password, baseURL, method;;
	private String browser;
	
	private static File file;
	private static XSSFWorkbook testInfo;
	private static XSSFSheet result;
	private static XSSFSheet info;
	private static XSSFCell cell;
	private static XSSFCellStyle center;
	private static XSSFFont redFont;
	private static XSSFCellStyle red;
	
	private int index;
	private String ID;
	private String pixel;
	private String payin;
	private String link;
	private String country;
	private String countryCode;
	private String cookie;
	private String testCampaignID;
	private String testCreativeID;
	private String testAffiliateID;
	private boolean redirecting;
	private String server;
	private String ipAddr;
	private String results;
	String filePath;
	
	private static List<String> campID = new ArrayList<String>();
	
	public Campaign(WebDriver driver, String baseURL){
		this.driver = driver;
		this.baseURL = baseURL;
		
	}
		
	//constructor, collect campaign information.
	Campaign(int index){
		
		setIndex(index);
		setID(campID.get(index));
		setLink(campID.get(index));
		setPayin();
		setPixel();
		setCountry();	
		
	}
	
	//constructor, create excel file/sheet/row/cell, and styles.
	Campaign(WebDriver driver, userLogin user, String filePath){
		
		setDriver(driver);
		this.filePath = filePath;
		userId = user.getUserId();
		password = user.getPassword();
		baseURL = user.getBaseUrl();
		method = user.getMethod();
		
		login();

		if(method.equals("Top 10")){
			camIdTop10();
		}
		else{
			CamIdFromFile();
		}
		
		file = new File(filePath+"Temp.xlsx");

		try{
			if(file.exists()){
			file.delete();
			}
		}catch(Exception e){
			System.out.println("Creaing file error!!!");
		}

		testInfo = new XSSFWorkbook();
		result = testInfo.createSheet("RESULT");
		info = testInfo.createSheet("INFO");
		

		redFont = testInfo.createFont();
		redFont.setColor(HSSFColor.RED.index);	  

		red = testInfo.createCellStyle();
		red.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		red.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		red.setFont(redFont);
		
		center = testInfo.createCellStyle();
		center.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		center.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		
		for(int i = 0; i < campID.size(); i++){
			XSSFRow row1, row2;

			row1 = info.createRow(i);
			row2 = result.createRow(i);
			for(int j = 0; j < 11; j++){
				row1.createCell(j);
				cell = row2.createCell(j);
				cell.setCellValue("");
				cell.setCellStyle(center);
			}
		}			
	}
	
	public void setDriver(WebDriver o_driver){
		driver = o_driver;
	}
	
	public void setBrowserName(){
		
		if(driver instanceof ChromeDriver){
			this.browser = "Chrome";
		}
		else{
			this.browser = "FF";
		}
	}
	
	public void login() {
		
		driver.get(baseURL+"admin/login");
		//driver.manage().window().maximize();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(userId);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login")).click();
		String selectLoginXpath = "html/body/div[2]/div/div/div[2]/table/tbody/tr/td[1]/a";
		waitForElementByXpath(30, selectLoginXpath);
		driver.findElement(By.xpath(selectLoginXpath)).click();
		String statsTabXpath = ".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]";
		waitForElementByXpath(30, statsTabXpath);
	  
	}
	
	private void camIdTop10(){
		 
		int numOfClicks;
		int numOfLeads;
/*
		String statsTabXpath = ".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]";
		waitForElementByXpath(60, statsTabXpath);	  
		driver.findElement(By.xpath(statsTabXpath)).click();

		String statsSubTabXpath = ".//*[@id='menu-authentication-bar']/ul/li[1]/ul/li[1]/a";
		waitForElementByXpath(60, statsSubTabXpath);
		driver.findElement(By.xpath(statsSubTabXpath)).click();*/
		
		driver.get("http://www.mm-tracking.com/admin/stats/campaign");

		int count = 0;
		int row = 3;

		while(count < 10){
			
			//get numbers of clicks and leads, remove the comma(,), then convert it to integer
			String clickXpath = ".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[13]";
			waitForElementByXpath(60, clickXpath);
			
			numOfClicks = Integer.parseInt(driver.findElement(By.xpath(clickXpath)).getText().replaceAll(",", ""));
			numOfLeads = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[16]/span")).getText().replaceAll(",", ""));

			//get campaign IDs, skill Pay per Call type(leads < click) 
			if(numOfLeads > numOfClicks){			
				row++; 
			}
			else{
				campID.add(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[3]")).getText());
				count++;
				row++;
			}
						
		}
	 
	}
	
	private void CamIdFromFile(){
		 
		File file = new File(filePath+"CampList.csv");
		try{
			
			Scanner sc = new Scanner(file);
			
			while(sc.hasNextLine()){
			
				campID.add((sc.next()));
		
			}
		
			sc.close();
		}
		catch(Exception e){
		
			System.out.println("File is not found!");
		
		}

	}
	
	
	public List<String> getCampId(){
		return campID;
	}
	

	public void setTestInfo(){
		checkJsPopup();
		setBrowserName();
		setCookie();
		setTestCampaignID();
		setTestCreativeID();
		setTestAffiliateID();
		setServer(link);
	}
	
	private void setIndex(int index){
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getSize(){
		return campID.size();
	}
		
	private void setID(String cid){
		ID = cid;
	}
	
	public String getID(){
		return ID;
	}
	
	private void setLink(String cId){
		
		link = "temp";
		while(link.equals("temp")){
			String testLinkXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[11]/td[2]/a";
			
			driver.get(baseURL+camUrl+cId);
			
			try {
				//check if the the "Regular" radio button exists
				waitForElementByXpath(60,testLinkXpath);
				link = driver.findElement(By.xpath(testLinkXpath)).getAttribute("href").replaceAll("&.+", "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(cId+" get link issue, trying again!");
			}
		}
		
	}
	
	public String getLink(){
		return link;
	}
	
	private void setPayin(){
		
		String payinTypeXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[7]/td[2]";
		waitForElementByXpath(20, payinTypeXpath);
		 
		if(driver.findElement(By.xpath(payinTypeXpath)).getText().trim().equals("Variable"))
		 payin = "Variable";
		else
		 payin = "Fixed";
		
	}
	
	public String getPayin(){
		return payin;
	}
	
	private void setPixel(){

		String pixelTypeXpath = ".//*[@id='ui-tabs-1']/div[2]/div/div/table/tbody/tr/td[1]/table/tbody/tr[15]/td[1]";
		waitForElementByXpath(20, pixelTypeXpath);
			
		if(driver.findElement(By.xpath(pixelTypeXpath)).getText().trim().equals("Image Pixel:"))
			pixel = "Regular";
		else
			pixel = "S2S";

	}
	
	public String getPixel(){
		return pixel;
	}
	
	private void setCountry(){
		
		//click Details tab in pop up window
		String detailsTabXpath = ".//*[@id='mundoTabs']/ul/li[2]/a";
		waitForElementByXpath(20,detailsTabXpath);
		driver.findElement(By.xpath(detailsTabXpath)).click();
		
		//Country List radio button selected. If there are multiple countries, randomly pick 1 from the list
		String geoRadioBtn2Xpath = ".//*[@id='DATA-geotracking-1']";
		String selectedCountryXpath = ".//*[@id='geotracking_countries_selected']/option[1]";
		waitForElementByXpath(20, geoRadioBtn2Xpath);
		
	  	List<String> countryAndCode = new ArrayList<String>();
		
		if(driver.findElement(By.xpath(geoRadioBtn2Xpath)).isSelected()) {
			
			waitForElementByXpath(45, selectedCountryXpath);
		 
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
		
		countryCode = countryAndCode.get(0);
		country = countryAndCode.get(1);
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getCountryCode(){
		return countryCode;
	}
	
	private void setCookie(){

		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/table/tbody/tr[3]/td[2]")));
		cookie = driver.manage().getCookieNamed("mt_lds").getValue();
		
	}
	
	public String getCookie(){
		return cookie;
	}
	
	private void setTestCampaignID(){
		
		String testCampaignIDXpath = "html/body/table/tbody/tr[1]/td[2]";
		
		//get IDs in pixel Test page
		waitForElementByXpath(120, testCampaignIDXpath);
	  
		String id = driver.findElement(By.xpath(testCampaignIDXpath)).getText();
	  
		if(id == null || id.isEmpty()){
		  
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.navigate().refresh();
			
			waitForElementByXpath(60, testCampaignIDXpath);  
			id = driver.findElement(By.xpath(testCampaignIDXpath)).getText();
			
			if(id.equals("") || id.isEmpty()){
				
				System.err.println("Tracking Issue!! Please check the MT system!!");
			  
			}
		  
		}
		
		testCampaignID = driver.findElement(By.xpath(testCampaignIDXpath)).getText(); 
	}
	
	public String getTestCampaignID(){
		return testCampaignID;
	}
	
	private void setTestCreativeID(){
		 
		String testCreativeIDXpath = "html/body/table/tbody/tr[2]/td[2]";
		
		testCreativeID = driver.findElement(By.xpath(testCreativeIDXpath)).getText();  
	}
	
	public String getTestCreativeID(){
		return testCreativeID;
	}
	
	private void setTestAffiliateID(){
		
		String testAffiliateIDXpath = "html/body/table/tbody/tr[3]/td[2]";
		
		testAffiliateID = driver.findElement(By.xpath(testAffiliateIDXpath)).getText();
		
	}
	
	public String getTestAffiliateID(){
		return testAffiliateID;
	}
	
	public void setRedirectStatus(boolean status){
		
		redirecting = status;
	}
	
	public boolean getRedirectStatus(){
		return redirecting;
	}
	
	private void setServer(String testlink){
		
		if(testlink.contains("qa-dev")){
			server="N/A";
		}
		else{
			String tmpLink = testlink.replaceAll("mt/.+", "");
			
			openNewTab(driver);
			driver.get(tmpLink+"info.php");
			  
			String serverNameXpath = "html/body";
			waitForElementByXpath(30,serverNameXpath);
			String tempName = driver.findElement(By.xpath(serverNameXpath)).getText();
			closeTab(driver);
			
			//Capitalize the first letter and return
			server = tempName.substring(0,1).toUpperCase() + tempName.substring(1);
		}
		
	}
	
	public String getServer(){
		return server;
	}
	
	
	public void pixelTestTool(WebDriver o_driver, String pixelType){
		
		driver = o_driver;
		//checking pixel type
		if(pixelType.equals("Regular")){
 
			//Open the Pixel Test page
			driver.get(baseURL+regularPixelURL);
		}
		  
		else{		

			//Open the Pixel Test page
			driver.get(baseURL+s2sPixelURL);
		}
		  	  
	}
	  
	public void linkTest(String testLink){
		
		waitForElementByID(60, "test_link");
		
		//Clean all browser cache and cookies
		driver.manage().deleteAllCookies();
		//Enter the test link
		driver.findElement(By.id("test_link")).sendKeys(testLink);
		//Click "Start Test" button
		driver.findElement(By.id("start")).click();
		  
	}
	
	public void changeProxy(String testCountry){
	  
		//JOptionPane.showMessageDialog(null, "<html><body><p>Please set the Proxy server to <h2>"+ testCountry +"</h2></P></body></html>");
		JOptionPane op = new JOptionPane(
				new JLabel("<html><body><p>Please set the Proxy server to <h2>"+ testCountry +"</h2></P></body></html>"), JLabel.CENTER);
		JDialog dialog = op.createDialog("Message Dialog");
		dialog.setAlwaysOnTop(true);
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	
	}
	
	public void closeProxy(){
	  
		JOptionPane.showMessageDialog(null, "Test Done! Please disconnect the Proxy if it is still connected!");
	
	}
	
	public String checkRedirectType(String campID, String affID, String cookie){
		
		driver.navigate().to(baseURL+errorLogUrl);
		String errorName = "";
		
		String searchCampID = "search_campaign";
		String searchParnerID = "search_partner";
		
		waitForElementByID(30, searchCampID);
		
		try{
			driver.findElement(By.id(searchCampID)).sendKeys(campID);
			
			//Select campaign from popup list
			String idOfPopupXpath = "html/body/ul[3]";
			waitForElementByXpath(180, idOfPopupXpath);
			String matchingItemXpath = "html/body/ul[3]/li[1]";
			driver.findElement(By.xpath(matchingItemXpath)).click();
			
			driver.findElement(By.id(searchParnerID)).sendKeys(affID);
			Thread.sleep(2000);
			driver.findElement(By.id("submit")).click();
			
			String listTableID = "list_table";
			waitForElementByID(30, listTableID);
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
		
		return errorName.replaceAll("Click redirect - ", "");
		
	}
	
	public void fire(String payinType, String pixelType) {
		
		if(pixelType.equals("Regular")){
			String btnFirePixel = "html/body/form[1]/input";
			driver.findElement(By.xpath(btnFirePixel)).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//if end
			
		else{
			if(payinType.equals("Variable")){		
				//get the pixel URL and add 0.1 at the end for variable pay in type
				String pixelURL = driver.findElement(By.xpath(".//*[@id='pixel']")).getAttribute("value");
				driver.findElement(By.xpath(".//*[@id='pixel']")).clear();
				driver.findElement(By.xpath(".//*[@id='pixel']")).sendKeys(pixelURL+"0.1");				
			}
			
			
/*			//test
			String pixelURL = driver.findElement(By.xpath(".//*[@id='pixel']")).getAttribute("value");
			driver.findElement(By.xpath(".//*[@id='pixel']")).clear();
			pixelURL = pixelURL.replaceAll("lead.+com", "www.mlinktracker.com");
			driver.findElement(By.xpath(".//*[@id='pixel']")).sendKeys(pixelURL);	*/	
			
			//Click button to fire the pixel
			driver.findElement(By.xpath(".//*[@id='fire']")).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}//else end
	}
	
	public boolean checkLead(String cookies, String campaignID){
		
		boolean lead = false;
		driver.navigate().to(baseURL+leadRptURL);
				
		try {
			String SearchCidXpath = ".//*[@id='search_campaign']";
			waitForElementByXpath(30, SearchCidXpath);
			driver.findElement(By.xpath(SearchCidXpath)).clear();
			driver.findElement(By.xpath(SearchCidXpath)).sendKeys(campaignID);
			Thread.sleep(2000);
			
			String SearchPartnerXpath = ".//*[@id='search_partner']";
			driver.findElement(By.xpath(SearchPartnerXpath)).clear();
			driver.findElement(By.xpath(SearchPartnerXpath)).sendKeys("5");  
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		driver.findElement(By.xpath(".//*[@id='button_confirm']")).click();
		
		String statsTableXpath = ".//*[@id='stats_table']/table";
		waitForElementByXpath(30, statsTableXpath);
		WebElement statsTable = driver.findElement(By.xpath(statsTableXpath));
		
		try{
			List<WebElement> leadList = statsTable.findElements(By.partialLinkText(affiliateLinkText));
			
			for(int i=3; i < leadList.size()+3; i++){
				
				String cookieID = driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+i+"]/td[7]")).getText();
				
				if(cookies.contains(cookieID)){
					setIP(i);
					lead = true;
				}
			}
			
			
		}
		catch(Exception e){
		
		}
		
		return lead;
	}
	
	public void setIP(int leadRow){
		ipAddr = driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+leadRow+"]/td[5]")).getText();
	}
	
	public String getIP(){
		
		return ipAddr;
		
	}
	
	public void setResults(String results){
		this.results = results;
	}
	
	public String getResults(){
		return results;
	}
	
	public void toInfo(int row){
		write(row,0,getID(),"info");
		write(row,1,getLink(),"info");
		write(row,2,getPayin(),"info");
		write(row,3,getPixel(),"info");
		write(row,4,getCountryCode(),"info");
		write(row,5,getCountry(),"info");
		write(row,6,getCookie(),"info");
		write(row,7,getTestCampaignID(),"info");
		write(row,8,getTestCreativeID(),"info");
		write(row,9,getTestAffiliateID(),"info");
		write(row,10,getServer(),"info");
		streamToFile();	
	}
	
	public void toResultWithLead(int row){
		
		write(row,0,date,"result");

		write(row,1,"5","result");

		write(row,2,ID,"result");

		write(row,3,testCreativeID,"result");

		write(row,4,countryCode+"/"+ipAddr,"result");

		write(row,5,browser,"result");

		write(row,6,pixel,"result");

		write(row,7,results,"result");

		write(row,8,antiVirus,"result");

		write(row,9,server,"result");

		write(row,10,note,"result");

		streamToFile();	
	
	}
	
	public void toResultRedirecting(int row){

		write(row,0,date,"result");
		cell.setCellStyle(red);
		
		write(row,1,"5","result");
		cell.setCellStyle(red);
		
		write(row,2,ID,"result");
		cell.setCellStyle(red);
		
		write(row,3,"","result");

		write(row,4,"","result");

		write(row,5,browser,"result");

		write(row,6,pixel,"result");

		write(row,7,results,"result");

		write(row,8,antiVirus,"result");

		write(row,9,server,"result");

		note = checkRedirectType(ID, testAffiliateID, cookie);
		write(row,10,note,"result");
		cell.setCellStyle(red);
		
		streamToFile();
	}
	
	public void toResultNoLead(int row){
		
		write(row,0,date,"result");
		cell.setCellStyle(red);
		
		write(row,1,"5","result");
		cell.setCellStyle(red);
		
		write(row,2,ID,"result");
		cell.setCellStyle(red);
		
		write(row,3,"","result");
		
		write(row,4,"","result");
		
		write(row,5,browser,"result");

		write(row,6,pixel,"result");

		write(row,7,results,"result");
		cell.setCellStyle(red);
		
		write(row,8,antiVirus,"result");

		write(row,9,server,"result");

		note = checkRedirectType(ID, testAffiliateID, cookie);
		write(row,10,note,"result");
		cell.setCellStyle(red);
		
		streamToFile();
	}
	
	public void waitForElementByXpath(int waitSecond, String Xpath){
	 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
		
	public void waitForElementByID(int waitSecond, String ID){
	 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	 
	}
	
	public void openNewTab(WebDriver driver){
		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
	
	}
	
	public void closeTab(WebDriver driver){
		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
		driver.switchTo().defaultContent();
	
	}
	
	public void checkJsPopup(){
		
		String originalWindow;
		String popWindow;
		
		//store the original window info
		originalWindow = driver.getWindowHandle();
		popWindow = null;
		
		//check if pop up window
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		while(it.hasNext()){
			popWindow = it.next();
		}
		
		if(!originalWindow.equals(popWindow)){
			//switch the focus to pop up window
			driver.switchTo().window(popWindow);
			
			//way to close the JavaScript pop up confirmation
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript("window.onbeforeunload = function() {};");
			
		    //switch the focus back to main window
			driver.switchTo().window(originalWindow);
		}
		
	}

	public void write(int i, int j, String o, String sheet){
		
		XSSFSheet tempSheet;
		if(sheet.equals("info")){
			tempSheet = info;
		}
		else{
			tempSheet = result;
		}
		cell = tempSheet.getRow(i).getCell(j);
		cell.setCellValue(o);
	}	
	
	public void streamToFile(){
		
		//Write to Excel, check if file is open, ask to close
		String status = null;
		while(status == null || status.equals("notClosed")){
		
			try {
				FileOutputStream out = new FileOutputStream(file);
				testInfo.write(out);
				out.close();
				status = "Closed";
			} 
			catch (Exception e) {
				status = "notClosed";
				JOptionPane.showMessageDialog(null, "Please close the Excel file then click OK!");
			}
			
		}
	
	}
	
	public class comp1 implements Comparator<Campaign>{
		public int compare(Campaign o1, Campaign o2) {
			// TODO Auto-generated method stub
			return o1.getCountry().compareTo(o2.getCountry());
		}
	}
	
	public class comp2 implements Comparator<Campaign>{
		public int compare(Campaign o1, Campaign o2) {
			// TODO Auto-generated method stub
			return Integer.compare(o1.getIndex(), o2.getIndex());
		}
	}

		
	public void tearDown() {
		 
		while(driver != null){
			driver.quit();
			closeProxy();
			System.exit(1);
		}
	}
	
}
