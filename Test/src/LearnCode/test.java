package LearnCode;

import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class test{
	
	static WebDriver driver;
	static String country;
	static String testLink;
	static String domain;
	
	public static void main(String[] args) {
		test obj = new test();
		
		testLink = JOptionPane.showInputDialog("Enter the test link:");
		domain = JOptionPane.showInputDialog("Enter the domain");
		
		testLink = testLink.replaceAll("http.+.com", "http://"+domain);
		
		do{
			country = JOptionPane.showInputDialog(null,"Enter the target Country:");
			
			driver = new FirefoxDriver();
			driver.get("http://leads.mlinktracker.com/tools/s2s_test.php");
			
			obj.waitForElementByID(60, "test_link");
			
			//Clean all browser cache and cookies
			driver.manage().deleteAllCookies();
	
			//Enter the test link
			driver.findElement(By.id("test_link")).sendKeys(testLink+"&subid1=qa-test-"+country.trim());
			//Click "Start Test" button
			driver.findElement(By.id("start")).click();
			
			obj.waitForElementByXpath(120, "html/body/table/tbody/tr[1]/td[1]");
			
			String currentURL = driver.getCurrentUrl();
			String newURL = currentURL.replaceAll("pixeltrack66.com", domain);
			
			driver.navigate().to(newURL);
			
			obj.waitForElementByXpath(120, "html/body/table/tbody/tr[1]/td[1]");
			
			String currentPixel = driver.findElement(By.xpath(".//*[@id='pixel']")).getAttribute("value");
			driver.findElement(By.xpath(".//*[@id='pixel']")).clear();
			String newPixel = currentPixel.replaceAll("OPTIONAL_INFORMATION", country.trim());
			driver.findElement(By.xpath(".//*[@id='pixel']")).sendKeys(newPixel);	
	
			driver.findElement(By.xpath(".//*[@id='fire']")).click();
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			
			driver.quit();
		}while(!country.toLowerCase().equals("Vietnam".toLowerCase()));

	}
	
	public void waitForElementByXpath(int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
	
	public void waitForElementByID(int waitSecond, String ID){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	 
	}
	
	

}