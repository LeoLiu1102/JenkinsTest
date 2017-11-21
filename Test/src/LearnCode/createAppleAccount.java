package LearnCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class createAppleAccount {
	
	WebDriver driver;
	String email = "test@test.com";
	
	public static void main(String[] args) throws IOException {

		createAppleAccount obj = new createAppleAccount();
		obj.create();

	}
	
	public void create() throws IOException{
		
		driver = new FirefoxDriver();
		driver.get("https://appleid.apple.com/account#!&page=create");
		
		//input email
		String emailXpath = ".//*[@id='widget']/div[1]/div/div[2]/account-name/div/div/div/div/email-input/div/input";
		waitForElementByXpath(60, emailXpath);
		
		driver.navigate().refresh();
		
		waitForElementByXpath(60, emailXpath);
		driver.findElement(By.xpath(emailXpath)).sendKeys(email);
		
		//input password
		driver.findElement(By.xpath(".//*[@id='password']")).sendKeys("!Mundo123");
		
		//input confirm password
		String confirmPasswordXpath = ".//*[@id='widget']/div[1]/div/div[2]/div[1]/confirm-password/div/div/div/confirm-password-input/div/input";
		waitForElementByXpath(60, confirmPasswordXpath);
		driver.findElement(By.xpath(confirmPasswordXpath)).sendKeys("!Mundo123");
		
		WebElement ele = driver.findElement(By.xpath(".//*[@id='widget']/div[1]/div/div[5]/create-captcha/div/div/div/div/div[1]/div/idms-captcha/div/img"));
		screenshot(ele);
		frame fr = new frame();
		String vCode = fr.verificationCode;
		System.out.println(vCode);

	}
	
	public void screenshot(WebElement ele) throws IOException{
		
		File screenshot = ((TakesScreenshot)this.driver).getScreenshotAs(OutputType.FILE);

		//Get the location of element on the page
		Point point = ele.getLocation();
		//Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		
		BufferedImage  fullImg = ImageIO.read(screenshot);
		//Crop the entire page screenshot to get only element screenshot
		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		eleScreenshot = resize(eleScreenshot,300, 200);
		ImageIO.write(eleScreenshot, "jpg", screenshot);
		//Copy the element screenshot to disk
		File screenshotLocation = new File("C:/Users/qauser/Desktop/Verify_Picture_screenshot.jpg");
		FileUtils.copyFile(screenshot, screenshotLocation);
		
		//showPic();
	}
	
	public void showPic(){
		
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		ImageIcon icon = new ImageIcon("C:/Users/qauser/Desktop/Verify_Picture_screenshot.jpg");
		JLabel label = new JLabel();
		JLabel label2 = new JLabel("Type the characters in the image");
		
		JTextField txtbox = new JTextField(4);
		Font f = new Font(txtbox.getText(), 50, 30);

		txtbox.setFont(f);
		
		label.setIcon(icon);
		panel.add(label);
		panel.add(label2);
		panel.add(txtbox);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
	}
	
	public BufferedImage resize(BufferedImage img, int newW, int newH) {
		//Getting the width and height of the given image.  
		int w = img.getWidth();
		  int h = img.getHeight();
		//Creating a new image object with the new width and height and with the old image type
		  BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		  Graphics2D g = dimg.createGraphics();
		  g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//Creating a graphics image for the new Image.
		  g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		  g.dispose();
		  return dimg;
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
