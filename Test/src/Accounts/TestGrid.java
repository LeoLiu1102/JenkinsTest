package Accounts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestGrid extends Functions{
	
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
	
	
	TestGrid(WebDriver driver, String id) throws InterruptedException{
		
		this.campID = id;
		this.link = GetLink(driver, id);
		this.payinType = GetPayinType(driver);
		this.pixelType = GetPixelType(driver);
		this.countryCode = GetCountryandCode(driver,id).get(0);
		this.country = GetCountryandCode(driver,id).get(1);
		
	}
	
	TestGrid(String cookie, String server, String testCampID, String testCreativeID, String testAffiliateID){
		
		this.cookie = cookie;
		this.server = server;
		this.testCampID = testCampID;
		this.testCreativeID = testCreativeID;
		this.testAffiliateID = testAffiliateID;

		
	}
	
	
	public static void main (String []arg) throws Exception{

		DesiredCapabilities driverF = DesiredCapabilities.chrome();

		DesiredCapabilities driverS = DesiredCapabilities.chrome();
		//driverF.setBrowserName("Firefox");
		//driverF.setVersion(Version.VERSION);
		//driverF.setPlatform(Platform.WINDOWS);
		//Functions test = new Functions();
		
		RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), driverF, driverS);
		
				
				NewDHC test = new NewDHC();
				//WebDriver driver = new FirefoxDriver();
				
				List<String> camID = new ArrayList<String>();
				List<String> countryInfo = new ArrayList<String>();
				String date = String.format("%td-%<tb-%<ty", new Date());	
					
				test.Login(driver);
				
				camID = test.GetCamIdTop10(driver);
				//camID = test.GetCamIdFromFile();
				
				File file = new File("C:/Users/qauser/Desktop/Output.xlsx");
				try{
					if(file.exists()){
					file.delete();
				}
				}catch(Exception e){
					System.out.println("Creaing file error!!!");
				}

				XSSFRow row[] = new XSSFRow[camID.size()];

				XSSFWorkbook testInfo = new XSSFWorkbook();
				XSSFSheet result = testInfo.createSheet("RESULT");
				XSSFSheet info = testInfo.createSheet("INFO");
				

				XSSFFont redFont = testInfo.createFont();
				redFont.setColor(HSSFColor.RED.index);	  

				XSSFCellStyle red = testInfo.createCellStyle();
				red.setFont(redFont);
				
				
				for(int i = 0; i < camID.size(); i++){
					
					row[i] = info.createRow(i);
					
					for(int j = 0; j < 12; j++){
						
						XSSFCell cell = row[i].createCell(j);
						
						switch(j){
						
							case 0: cell.setCellValue(camID.get(i));
									break;
									
							case 1: cell.setCellValue(test.GetLink(driver, camID.get(i)));
									break;
							
							case 2: cell.setCellValue(test.GetPayinType(driver));
									break;
							
							case 3: cell.setCellValue(test.GetPixelType(driver));
									break;
							
							case 4: countryInfo = test.GetCountryandCode(driver, camID.get(i));
									cell.setCellValue(countryInfo.get(0));
									break;
									
							case 5: cell.setCellValue(countryInfo.get(1));
									break;
									
							default: break;
								
						
						}
						
						
					}		
					
					//Write to Excel, check if file is open, ask to close
					String status = null;
					while(status == null || status.equals("notClosed")){
						
						status = test.streamToFile(file, testInfo, status);
						
					}

				}
				
				driver.quit();
				
				//Test US campaigns
				int count = 1;
				for(int i = 0; i < camID.size(); i++){
					
					XSSFCell cell;
								
					if(info.getRow(i).getCell(4).toString().equals("US")){
						
						driver = new FirefoxDriver();
						//driver = new ChromeDriver();
						
						test.pixelTestTool(driver, info.getRow(i).getCell(3).toString());				
						
						if(count == 1){
							
							test.ChangeProxy(info.getRow(i).getCell(5).toString());
						
						}
						count++;
						
						test.linkTest(driver, info.getRow(i).getCell(1).toString());
						
						cell = info.getRow(i).getCell(6);
						cell.setCellValue(test.GetCookies(driver));
						
						cell = info.getRow(i).getCell(7);
						cell.setCellValue(test.getTestCampaignID(driver));
						
						cell = info.getRow(i).getCell(8);
						cell.setCellValue(test.getTestCreativeID(driver));
						
						cell = info.getRow(i).getCell(9);
						cell.setCellValue(test.getTestAffID(driver));
									
						cell = info.getRow(i).getCell(10);
						cell.setCellValue(test.GetServer(driver, info.getRow(i).getCell(1).toString()));
						
						String campaignID = camID.get(i);
						String testCampID = info.getRow(i).getCell(7).toString();
						String testAffiID = info.getRow(i).getCell(9).toString();
						
						if(campaignID.equals(testCampID) && testAffiID.equals("5")){
							
							cell = info.getRow(i).getCell(11);
							cell.setCellValue("Not Redirecting");
							
							String payinType = info.getRow(i).getCell(2).toString();
							String pixelType = info.getRow(i).getCell(3).toString();
							
							test.Fire(driver, payinType, pixelType);
							
						}
						else{
							
							cell = info.getRow(i).getCell(11);
							cell.setCellValue("Redirecting");
							
						}
						
						String status = null;
						while(status == null || status.equals("notClosed")){
							
							status = test.streamToFile(file, testInfo, status);
							
						}

						driver.quit();
						
					}
					
				}
				
				//Test Non US campaigns
				for(int i = 0; i < camID.size(); i++){
					
					XSSFCell cell;
					
					if(!info.getRow(i).getCell(4).toString().equals("US")){
						
						driver = new FirefoxDriver();
						//driver = new ChromeDriver();
						
						test.pixelTestTool(driver, info.getRow(i).getCell(3).toString());
						
						test.ChangeProxy(info.getRow(i).getCell(5).toString());
						
						test.linkTest(driver, info.getRow(i).getCell(1).toString());
						
						cell = info.getRow(i).getCell(6);
						cell.setCellValue(test.GetCookies(driver));
						
						cell = info.getRow(i).getCell(7);
						cell.setCellValue(test.getTestCampaignID(driver));
						
						cell = info.getRow(i).getCell(8);
						cell.setCellValue(test.getTestCreativeID(driver));
						
						cell = info.getRow(i).getCell(9);
						cell.setCellValue(test.getTestAffID(driver));
									
						cell = info.getRow(i).getCell(10);
						cell.setCellValue(test.GetServer(driver, info.getRow(i).getCell(1).toString()));
						
						String campaignID = camID.get(i);
						String testCampID = info.getRow(i).getCell(7).toString();
						String testAffiID = info.getRow(i).getCell(9).toString();
						
						if(campaignID.equals(testCampID) && testAffiID.equals("5")){
							
							cell = info.getRow(i).getCell(11);
							cell.setCellValue("Not Redirecting");
							
							String payinType = info.getRow(i).getCell(2).toString();
							String pixelType = info.getRow(i).getCell(3).toString();
							
							test.Fire(driver, payinType, pixelType);
							
						}
						else{
							
							cell = info.getRow(i).getCell(11);
							cell.setCellValue("Redirecting");
							
						}
						
						String status = null;
						while(status == null || status.equals("notClosed")){
							
							status = test.streamToFile(file, testInfo, status);
							
						}
						
						driver.quit();
						
					}
								
				}
				
				test.CloseProxy();
				
				driver = new FirefoxDriver();
				//driver = new ChromeDriver();
				
				test.Login(driver);
				
				for(int i = 0; i < camID.size(); i++){
								
					XSSFRow newRow = result.createRow(i);
					XSSFCell dateCell = newRow.createCell(0);
					XSSFCell affidCell = newRow.createCell(1);
					XSSFCell camidCell = newRow.createCell(2);
					XSSFCell creativeCell = newRow.createCell(3);
					XSSFCell proxyCell = newRow.createCell(4);
					XSSFCell browserCell = newRow.createCell(5);
					XSSFCell leadCell = newRow.createCell(6);
					XSSFCell resultCell = newRow.createCell(7);
					XSSFCell antiCell = newRow.createCell(8);
					XSSFCell serverCell = newRow.createCell(9);
					XSSFCell noteCell = newRow.createCell(10);
					
					dateCell.setCellValue(date);
					if(info.getRow(i).getCell(11).toString().equals("Redirecting")){
						dateCell.setCellStyle(red);
					}
					
					affidCell.setCellValue(info.getRow(i).getCell(9).toString());
					if(info.getRow(i).getCell(11).toString().equals("Redirecting")){
						affidCell.setCellStyle(red);
					}
					
					camidCell.setCellValue(camID.get(i));
					if(info.getRow(i).getCell(11).toString().equals("Redirecting")){
						camidCell.setCellStyle(red);
					}
					
					browserCell.setCellValue("FF");			
					leadCell.setCellValue(info.getRow(i).getCell(3).toString());			
					antiCell.setCellValue("Symantec Endpoint Protection");			
					serverCell.setCellValue(info.getRow(i).getCell(10).toString());
					
					if(info.getRow(i).getCell(11).toString().equals("Not Redirecting")){
						
						int leadRow = test.CheckLead(driver, info.getRow(i).getCell(6).toString(), camID.get(i));
						
						if(leadRow > 0){
							
							creativeCell.setCellValue(info.getRow(i).getCell(8).toString());
							proxyCell.setCellValue(info.getRow(i).getCell(4).toString() + "/" + test.GetIP(driver, leadRow));
							resultCell.setCellValue("PASS");
							noteCell.setCellValue("");
							
						}
						else{
							
							System.err.println("TEST FAILED! Campaign "+camID.get(i)+" No lead Tracked.");
							resultCell.setCellValue("FAIL");
							resultCell.setCellStyle(red);
							noteCell.setCellValue("TEST FAILED! Campaign "+camID.get(i)+" No lead Tracked.");
							noteCell.setCellStyle(red);
							
						}
						
					}
					else{
						
						String error;
						String affID = info.getRow(i).getCell(9).toString();
						String cookie = info.getRow(i).getCell(6).toString();
						
						error = test.checkRedirectType(driver, camID.get(i), affID, cookie);
						System.err.println("Campaign "+camID.get(i)+" error type " + error);
						resultCell.setCellValue("PASS");
						
						noteCell.setCellValue(error);
						noteCell.setCellStyle(red);
						
					}
					

					String status = null;
					while(status == null || status.equals("notClosed")){
						
						status = test.streamToFile(file, testInfo, status);
						
					}
					
				}
				
				testInfo.close();
				
				test.tearDown(driver);

			}
	
		
}
	
	

