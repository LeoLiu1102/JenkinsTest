package TestCode2;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AddLeadsUsingTimeStamp {
	
	String[] pubID = {"88980", "88981", "88982", "88983", "88977", "89022"};
	String[] pixelLink = 
		{
				"http://qa-dev.dev-mt01/lead/e2c4w2a474x223v2/171895&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2u2x2/201117&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2u2y2/201118&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2u2z2/201119&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2v2q2/201120&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2v2r2/201121&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2v2s2/201122&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2v2t2/201123&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2v2u2/201124&aid=",
				"http://qa-dev.dev-mt01/lead/e2c4x23474q2v2v2/201125&aid="
		};
	WebDriver driver;
	int lead;
	
	@BeforeTest
	public void beoreTest(){
		driver = new FirefoxDriver();
		
	}
	
	@Test(priority=2)
	public void a() throws InterruptedException {
		
		int startTime = 1487568600;
		int endTime = 1487655000;
		
		while(startTime < endTime){
			for(int i=0; i < pixelLink.length; i++){
				lead = (int)(Math.random() * 2);
				System.out.println(lead);
				for(int a=0; a<lead; a++){
					for(int j=0; j< pubID.length; j++){
						//System.out.println(pixelLink[i] + pubID[j] + "&leadTime=" + startTime);
						driver.get(pixelLink[i] + pubID[j] + "&leadTime=" + startTime);
						Thread.sleep(500);
					}

				}
			}
			
			startTime = startTime + 3600;
		}

	}
	
	@Test(enabled=true)
	public void b() throws InterruptedException {
		
		int startTime = 1488173400;
		int endTime = 1488259800;
		
		while(startTime < endTime){
			for(int i=0; i < pixelLink.length; i++){
				lead = (int)(Math.random() * 2);
				System.out.println(lead);
				for(int a=0; a<lead; a++){
					for(int j=0; j< pubID.length; j++){
						//System.out.println(pixelLink[i] + pubID[j] + "&leadTime=" + startTime);
						driver.get(pixelLink[i] + pubID[j] + "&leadTime=" + startTime);
						Thread.sleep(500);
					}

				}
			}
			
			startTime = startTime + 3600;
		}

	}
}
