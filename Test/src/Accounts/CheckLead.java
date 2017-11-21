package Accounts;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CheckLead extends Functions{
	
	
	public static void main (String []arg) throws Exception{
		
		WebDriver driver = new FirefoxDriver();
		CheckLead test = new CheckLead();
		
		String date = String.format("%td-%<tb-%<ty", new Date());
		
		
		File file = new File("C:/Users/qauser/Desktop/Output.xlsx");
		
		FileInputStream inputStream = new FileInputStream(file);
		
		XSSFWorkbook testInfo = new XSSFWorkbook(inputStream);
		XSSFSheet info = testInfo.getSheet("INFO");
		XSSFSheet result = testInfo.getSheet("RESULT");
		
		XSSFFont redFont = testInfo.createFont();
		redFont.setColor(HSSFColor.RED.index);	  

		XSSFCellStyle red = testInfo.createCellStyle();
		red.setFont(redFont);
		
		test.Login(driver);
		int rowCount = info.getLastRowNum();
		
		for(int i = 0; i <= rowCount; i++){
			
			String camID = info.getRow(i).getCell(0).toString();
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
			
			camidCell.setCellValue(camID);
			if(info.getRow(i).getCell(11).toString().equals("Redirecting")){
				camidCell.setCellStyle(red);
			}
			
			browserCell.setCellValue("FF");			
			leadCell.setCellValue(info.getRow(i).getCell(3).toString());			
			antiCell.setCellValue("Symantec Endpoint Protection");			
			serverCell.setCellValue(info.getRow(i).getCell(10).toString());
			
			if(info.getRow(i).getCell(11).toString().equals("Not Redirecting")){
				
				int leadRow = test.CheckLead(driver, info.getRow(i).getCell(6).toString(), camID);
				
				if(leadRow > 0){
					
					creativeCell.setCellValue(info.getRow(i).getCell(8).toString());
					proxyCell.setCellValue(info.getRow(i).getCell(4).toString() + "/" + test.GetIP(driver, leadRow));
					resultCell.setCellValue("PASS");
					noteCell.setCellValue("");
					
				}
				else{
					
					System.err.println("TEST FAILED! Campaign "+camID+" No lead Tracked.");
					resultCell.setCellValue("FAIL");
					resultCell.setCellStyle(red);
					noteCell.setCellValue("TEST FAILED! Campaign "+camID+" No lead Tracked.");
					noteCell.setCellStyle(red);
					
				}
				
			}
			else{
				
				String error;
				String affID = info.getRow(i).getCell(9).toString();
				String cookie = info.getRow(i).getCell(6).toString();
				
				error = test.checkRedirectType(driver, camID, affID, cookie);
				System.err.println("Campaign "+camID+" error type " + error);
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

		
	
	

