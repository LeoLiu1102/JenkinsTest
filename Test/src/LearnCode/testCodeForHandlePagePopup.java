package LearnCode;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class testCodeForHandlePagePopup {

	public static void main(String[] args) throws InterruptedException {
		
		
		System.setProperty("webdriver.chrome.driver", "C:/Users/qauser/Desktop/chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		
		//WebDriver driver = new FirefoxDriver();
		driver.get("file:///C:/Users/qauser/Desktop/test2.html");
		
		String btnID = "btn";
		waitForElementByID(driver, 30, btnID);
		driver.findElement(By.id(btnID)).click();

		Thread.sleep(3000);
		
        closePop(driver);
		
		String txtXpath = "html/body/h1";
		waitForElementByXpath(driver, 10, txtXpath);
		String txt = driver.findElement(By.xpath(txtXpath)).getText();
		System.out.println(txt);
		
		driver.get("http://www.google.ca");
		
		//driver.quit();
		
	}
	
	public static void closePop(WebDriver driver){
		
		String pw;
		String sw;
		
		//store the original window info
		pw = driver.getWindowHandle();
		sw = null;
		
		//check if pop up window
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		while(it.hasNext()){
			sw = it.next();
		}
		
		if(!pw.equals(sw)){
			//switch the focus to pop up window
			driver.switchTo().window(sw);
			
			//way to close the JavaScript pop up confirmation
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript("window.onbeforeunload = function() {};");
			
		    //switch the focus back to main window
			driver.switchTo().window(pw);
		}
		
	}
	
	public static boolean isAlertPresent(WebDriver driver){
		
		boolean status;
		
		WebDriverWait alertWait = new WebDriverWait(driver, 1);
	    
		try{
			if(alertWait.until(ExpectedConditions.alertIsPresent()) != null){
				status = true;
			}
			else{
				status = false;
			}
		}
		catch(Exception e){

			status = false;
		}
		
		
		return status;
	}
	
	
	public static void OpenNewTab(WebDriver driver){
		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
	
	}
	
	public static void CloseTab(WebDriver driver){
		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
		driver.switchTo().defaultContent();
	
	}
	
	public static String SwitchToPopupWindow(WebDriver driver){
	 
		//Switch to newly opened window
		String parentHandle = driver.getWindowHandle();
		 
		//get the current window handle
		for (String winHandle : driver.getWindowHandles()) {
			   	
			// switch focus of WebDriver to newly opened window)
			driver.switchTo().window(winHandle); 
			System.out.println(winHandle);  
		}//for loop end
		 
		return parentHandle;
	}
	
	
	public static void SwitchToMainWindow(WebDriver driver, String parentHandle){
	 
		driver.switchTo().window(parentHandle);
	 
	}
	
	public static void waitForElementByID(WebDriver driver, int waitSecond, String ID){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	 
	}
	
	public static void waitForElementByXpath(WebDriver driver, int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));
	 
	}
		

	

}
