package Proxy;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Proxy3 {

	static WebDriver driver;
	static String country[] = {"Japan", "United States", "Netherlands"};
	 
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 
		 ProfilesIni profile = new ProfilesIni();
		 
		 FirefoxProfile myProfile = profile.getProfile("Test");
		 
		 driver = new FirefoxDriver(myProfile);	

		 boolean alertCheck;
		 try{
			 driver.switchTo().alert();
			 alertCheck = true;
			 System.out.println("Alert panel exist");
		 }catch(Exception alertC){
			 alertCheck = false;
			 System.out.println("Alert panel doesn't exist");
		 }
		 
		 if(alertCheck){ 
			 
			 try{
				 Robot rb = new Robot();
				 
				 
				 StringSelection usr = new StringSelection("");	
				 Toolkit.getDefaultToolkit().getSystemClipboard().setContents(usr, null);
				 
/*				 Thread.sleep(500);
				 
				 rb.keyPress(KeyEvent.VK_CONTROL);
				 rb.keyPress(KeyEvent.VK_V);
				 rb.keyRelease(KeyEvent.VK_V);
				 rb.keyRelease(KeyEvent.VK_CONTROL);
				 
				 rb.keyPress(KeyEvent.VK_TAB);
				 rb.keyRelease(KeyEvent.VK_TAB);
				 
				 Thread.sleep(500);
				 
				 StringSelection pwd = new StringSelection("");	
				 Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pwd, null);
				 
				 rb.keyPress(KeyEvent.VK_CONTROL);
				 rb.keyPress(KeyEvent.VK_V);
				 rb.keyRelease(KeyEvent.VK_V);
				 rb.keyRelease(KeyEvent.VK_CONTROL);*/
				 
				 
				 Thread.sleep(3000);
				 
				 rb.keyPress(KeyEvent.VK_TAB);
				 rb.keyRelease(KeyEvent.VK_TAB);
				 
				 rb.keyPress(KeyEvent.VK_ENTER);			 
				 
				 
			 }catch(Exception e){
				 System.out.println("not working");
			 }
			 
		 }

		 
		 driver.get("http://leads.mlinktracker.com/tools/s2s_test.php");
		 
		 Proxy3 obj = new Proxy3();
		 obj.waitForElementByXpath(20, ".//*[@id='locationsform']/p/select");
		 Select drpCountry = new Select(driver.findElement(By.xpath(".//*[@id='locationsform']/p/select")));
		 drpCountry.selectByVisibleText(country[1]);
		 
		 String disconnectBtnXpath = ".//*[@id='buttons']/input";
		 obj.waitForElementByXpath(30, disconnectBtnXpath);
		 
		 String toolLinkXpath = ".//*[@id='middle-content']/p[2]/a";
		 obj.waitForElementByXpath(30, toolLinkXpath);
		 System.out.println("test line");
		 driver.findElement(By.xpath(toolLinkXpath)).click();

	}
	
	public void waitForElementByXpath(int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
}
