package TestNGScript;

import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;


public class FindAllLinks {
  @Test
  public void f() throws InterruptedException {
	  
	  WebDriver driver = new FirefoxDriver();
	  driver.get("http://mundoqatest.yolasite.com/");
	  Thread.sleep(5000);
	  List<WebElement> links = driver.findElements(By.tagName("a"));
	  List<URL> urls = new ArrayList<URL>();
	  for(WebElement ele:links){
		  try {
			urls.add(new URL(ele.getAttribute("href")));
		  } catch (MalformedURLException e) {
			System.out.println(ele);
		  }
	  }
	  System.out.println(links);
	  System.out.println(urls);
	  
	  for(int i=0; i<urls.size();i++){
		  try {
			HttpURLConnection conn = (HttpURLConnection) urls.get(i).openConnection();
			if (conn.getResponseCode() == 200) {
				System.out.println(urls.get(i) + " Test PASS!");
			}
			
			if (conn.getResponseCode() != 200) {
				System.out.println("Code "+conn.getResponseCode()+" "+urls.get(i) + " Test FAIL!");
				//throw new RuntimeException("HTTP error code:" + conn.getResponseCode());
			}
		  }catch(Exception e){
			  
		  }
	  }
  }
}
