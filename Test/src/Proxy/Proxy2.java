package Proxy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Proxy2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 WebDriver proxyDriver;
		 
		 String country[] = {"United States", "Netherlands"};

		 
		 for(int i=0; i< country.length; i++){
			 
			 ProfilesIni profile = new ProfilesIni();
			 
			 FirefoxProfile myProfile = profile.getProfile("Test");
			 
			 proxyDriver = new FirefoxDriver(myProfile);

			 proxyDriver.get("http://leads.mlinktracker.com/tools/s2s_test.php");

			 //Select target country
			 waitForElementByXpath(proxyDriver, 20, ".//*[@id='locationsform']/p/select");
			 Select drpCountry = new Select(proxyDriver.findElement(By.xpath(".//*[@id='locationsform']/p/select")));
			 drpCountry.selectByVisibleText(country[i]);
			 
			 //Click the target link
			 waitForElementByXpath(proxyDriver, 20, ".//*[@id='middle-content']/p[2]/a");
			 proxyDriver.findElement(By.xpath(".//*[@id='middle-content']/p[2]/a")).click();
			 
			 //Switch to new window
			 String mainWindow = SwitchToPopupWindow(proxyDriver);
			 
			 //Actions in new window.
			 waitForElementByID(proxyDriver, 20, "test_link");
			 proxyDriver.findElement(By.id("test_link")).sendKeys("http://kuaptrk.com/mt/1364v244b4w233t234w20344/");
			 proxyDriver.findElement(By.id("start")).click();
			 
			 waitForElementByXpath(proxyDriver, 20, "html/body/table/tbody/tr[1]/td[2]");
			 System.out.println(proxyDriver.findElement(By.xpath("html/body/table/tbody/tr[1]/td[2]")).getText());
			 
			 //Switch back to original window.
			 SwitchToMainWindow(proxyDriver, mainWindow);
			 
			 //click Disconnect button
			 waitForElementByXpath(proxyDriver, 20, ".//*[@id='buttons']/input");
			 proxyDriver.findElement(By.xpath(".//*[@id='buttons']/input")).click();
			 
			 //Wait disconnected then close all browser.
			 waitForElementByXpath(proxyDriver, 20, ".//*[@id='middle-content']/p/span/span");
			 String disconnect = proxyDriver.findElement(By.xpath(".//*[@id='middle-content']/p/span/span")).getText().trim();
			 
			 while(disconnect.equals("Disconnected")){
				 proxyDriver.quit();
				 break;
			 }
			 
		 }

		 
	}
	
	public static void waitForElementByXpath(WebDriver driver, int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
	
	public static void waitForElementByID(WebDriver driver, int waitSecond, String ID){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	 
	}
	
	  public static String SwitchToPopupWindow(WebDriver driver){
		  
		  //Switch to newly opened window
		  String parentHandle = driver.getWindowHandle();
		  
		  //get the current window handle
		  for (String winHandle : driver.getWindowHandles()) {
		    	
			  // switch focus of WebDriver to newly opened window)
			  driver.switchTo().window(winHandle); 
			  
		  } //for loop end
		  
		  return parentHandle;
	  }
	  public static void SwitchToMainWindow(WebDriver driver, String parentHandle){
		  
		  driver.switchTo().window(parentHandle);
		  
	  }


}
