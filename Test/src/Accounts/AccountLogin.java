package Accounts;

import java.io.File;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountLogin {
	
	private static Scanner x;
	private static String username;
	private static String password;
	static WebDriver driver;
	

	public static void main(String[] args) throws InterruptedException {
		
		openFile();
		readFile();
		closeFile();

	}
	

	
	public static void openFile(){
		
		try{
			x = new Scanner(new File("C:/Users/qauser/Desktop/EmailList.txt"));
		}
		catch(Exception e){
			System.out.println("File is not found!");
		}
	}
	
	
	public static void readFile() throws InterruptedException{
		
		
		while(x.hasNext()){
			
			username = x.next();
			password = x.next();
			logIn();
			
		}
		
	}
	
	public static void logIn() throws InterruptedException{

		File file= new File("C:/Users/qauser/Desktop/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());		
		driver = new ChromeDriver();
		driver.get("https://accounts.google.com/");
		
		waitForElementByXpath(20, ".//*[@id='Email']");
		driver.findElement(By.xpath(".//*[@id='Email']")).clear();
		driver.findElement(By.xpath(".//*[@id='Email']")).sendKeys(username);
		Thread.sleep(1000);
		driver.findElement(By.xpath(".//*[@id='next']")).click();
		Thread.sleep(1000);
		
		String errorMsgUsr = ".//*[@id='errormsg_0_Email']";
		if(driver.findElement(By.xpath(errorMsgUsr)).getText()
				.equals("Sorry, Google doesn't recognize that email")){
			System.out.println(username + " is not Recognized!");
		}
		else{

			waitForElementByXpath(20, ".//*[@id='Passwd']");
			driver.findElement(By.xpath(".//*[@id='Passwd']")).clear();
		
			if(driver.findElement(By.xpath(".//*[@id='PersistentCookie']")).isSelected())
				driver.findElement(By.xpath(".//*[@id='PersistentCookie']")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(".//*[@id='Passwd']")).sendKeys(password);
			Thread.sleep(1000);
			driver.findElement(By.xpath(".//*[@id='signIn']")).click();
			
			String errorMsgPwd = ".//*[@id='errormsg_0_Email']";
			if(driver.findElement(By.xpath(errorMsgPwd)).getText()
					.equals("The email and password you entered don't match.")){
				System.out.println(username + " Wrong Password!");
			}
			else{
				
				if(driver.findElement(By.xpath("html/body/div[2]/section/div/article/section/h1")).getText()
						.equals("Google Account disabled due to suspicious activity")){
					
						System.out.println(username + "is disabled");
					}

				if(doesElementExist(".//*[@id='login-challenge-heading']")){
						System.out.println(username + " needs to be verified");
					}				
				
				}
				
			}
		
				
		driver.close();
		
	}
	
	  
	public static void waitForElementByXpath(int waitSecond, String Xpath){
		  
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
		  
	}
	
	private static boolean doesElementExist (String myObject) {
		try {
		    driver.findElement(By.xpath(myObject));
		} catch (NoSuchElementException e) {
		    return false;
		}
		return true;
	}
	
	public static void closeFile(){
		x.close();
	}
	
	
}
