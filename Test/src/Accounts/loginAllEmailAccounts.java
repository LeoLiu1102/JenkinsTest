package Accounts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class loginAllEmailAccounts{
	
	public static void main (String []arg) throws Exception{
		
		WebDriver driver = new FirefoxDriver();
		loginAllEmailAccounts obj = new loginAllEmailAccounts();
		
		BufferedReader reader = new BufferedReader(new FileReader("C:/Users/qauser/Desktop/TestAccountList.csv"));
		
		String line = null;
		Scanner scanner = null;
		List<String> account = new ArrayList<>();
		List<String> passwd = new ArrayList<>();
		int index = 0;
		
		while((line = reader.readLine()) != null){
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			while(scanner.hasNext()){
				String data = scanner.next();
				if(index == 0){
					data.trim();
					account.add(data); 
				}
				else{
					data.trim();
					passwd.add(data);
				}
				index++;
			}
			index = 0;
		}
		
		reader.close();
		
		for(int i = 0; i < account.size(); i++){
			
		driver.get("https://accounts.google.com");
		driver.manage().window().maximize();
		
		System.out.println("Testing line "+ (i+1));
		
		String emailid = "Email";
		obj.waitForElementByID(driver, 60, emailid);
		driver.findElement(By.id(emailid)).sendKeys(account.get(i));
		
		String nextBtnid = "next";
		obj.waitForElementByID(driver, 20, nextBtnid);
		driver.findElement(By.id(nextBtnid)).click();
		
		String passid = "Passwd";
		obj.waitForElementByID(driver, 20, passid);
		driver.findElement(By.id(passid)).sendKeys(passwd.get(i));
		
		String singinBtnid = "signIn";
		obj.waitForElementByID(driver, 20, singinBtnid);
		driver.findElement(By.id(singinBtnid)).click();
		
		
		try{
		
			String iconXpath = ".//*[@id='gb']/div[1]/div[1]/div[2]/div[4]/div[1]/a/span";
			obj.waitForElementByXpath(driver, 30, iconXpath);
			driver.findElement(By.xpath(iconXpath)).click();
			driver.findElement(By.id("gb_71")).click();		
			driver.manage().deleteAllCookies();
			
		}catch(Exception e){
			
			try{
				
				//Recovery option		
				String doneBtnid = "view_container";
				obj.waitForElementByID(driver, 10, doneBtnid);
				driver.findElement(By.id(doneBtnid)).click();
				
			}catch(Exception e1){
				System.err.println("Line " + (i+1) + "   " + account.get(i) + " verification needed!");
			}
			
		}
					
		}
	
	}	
	
	public void waitForElementByID(WebDriver driver, int waitSecond, String ID){
		  
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
		  
	}
	
	public void waitForElementByXpath(WebDriver driver, int waitSecond, String xpath){
		  
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));  
		  
	}
	
	public void waitForTitleID(WebDriver driver, int waitSecond, String title){
		
		try{
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.titleIs(title));  
		}catch(Exception e){
			
		}
	}
	
}
