package DHC;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class testNewDriver {

	static WebDriver driver;
	static String country = "United States";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.gecko.driver","U:/Eclipse WorkSpace/DailyHealthCheck/Documents/geckodriver.exe");
		
		DesiredCapabilities cap = DesiredCapabilities.firefox();
		cap.setCapability("marionette", true);
		
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile myProfile = profile.getProfile("Test");
		
		driver = new FirefoxDriver(myProfile);
		driver.get("http://www.google.ca");
		
		 //Select target country
		 waitForElementByXpath(20, ".//*[@id='locationsform']/p/select");
		 Select drpCountry = new Select(driver.findElement(By.xpath(".//*[@id='locationsform']/p/select")));
		 drpCountry.selectByVisibleText(country);
		 
		 //Click the target link
		 waitForElementByXpath(20, ".//*[@id='middle-content']/p[2]/a");
		 driver.findElement(By.xpath(".//*[@id='middle-content']/p[2]/a")).click();

	}
	
	public static void waitForElementByXpath(int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}

}
