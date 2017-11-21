package Accounts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CheckAllTabs{
	
	
	public static void main (String []arg) throws Exception{
		
	Functions obj = new Functions();
	
	WebDriver driver = new FirefoxDriver();
	
	obj.Login(driver);
	
	WebElement TABbar;
	
	for(int i=0; i < 9; i++){
		
		obj.waitForElementByXpath(driver, 10, ".//*[@id='menu-authentication-bar']/ul");
		TABbar = driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul"));
		List<WebElement> Tab = TABbar.findElements(By.tagName("ul"));
		obj.waitForElementByXpath(driver, 10, ".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/div/a/div[2]");
		System.out.println(driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/div/a/div[2]")).getText());
		List<WebElement> subTab = Tab.get(i).findElements(By.tagName("a"));
	
		for(int j=1; j <= subTab.size(); j++){
			obj.waitForElementByXpath(driver, 10, ".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/div/a/div[2]");
			driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/div/a/div[2]")).click();
			System.out.println("	"+driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/ul/li["+j+"]/a")).getText());
			obj.waitForElementByXpath(driver, 10, ".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/ul/li["+j+"]/a");
			driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+(i+1)+"]/ul/li["+j+"]/a")).click();

		}

	}
	
	}
	
}
