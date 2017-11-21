package LearnCode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class createGoogleAccount implements ActionListener{
	
	JFrame frame;
	JPanel basePanel, leftPanel, rightPanel;
	JLabel title, emailLabel, countryLabel;
	JTextField emailTxtBox;
	JComboBox<String> envCombo, countryCombo;
	JButton createButton, cancelButton;
	
	static String country;
	static String email;
	static String altEmail;
	static String clickID;
	static WebDriver driver;

	public static void main(String[] args) throws IOException {

		createGoogleAccount obj = new createGoogleAccount();
		obj.createFrame();

	}
	
	public void start() throws FileNotFoundException{
		createGoogleAccount obj = new createGoogleAccount();
				
		String finalCode = "";
		String[] locales = Locale.getISOCountries();
		for(String countryCode:locales){
			Locale code = new Locale("", countryCode);
			if(code.getDisplayCountry().equals(country)){
				finalCode = code.getISO3Country();
				System.out.println(finalCode);
				break;
			}
		}
		if(finalCode.isEmpty()){
			JOptionPane.showMessageDialog(null, "Can't convert ISO3 code for this Country, Please check the Country name");
			System.exit(1);
		}
		
		email = "mundotest." + finalCode.toLowerCase();
		
		//System.setProperty("webdriver.chrome.driver", "C:/Users/qauser/Desktop/chromedriver.exe");

		//driver = new ChromeDriver();
		
		driver = new FirefoxDriver();
		driver.manage().deleteAllCookies();
		driver.get("https://accounts.google.com/SignUp");
		
		//wait for the page load
		String conXpath = ".//*[@id='CountryCode']/div[1]/div[2]";
		waitForElementByXpath(60, conXpath);
		
		//Input First and Last name
		driver.findElement(By.id("FirstName")).sendKeys("MundoTest");
		driver.findElement(By.id("LastName")).sendKeys(finalCode);
		
		driver.findElement(By.id("GmailAddress")).sendKeys(email);
		driver.findElement(By.xpath(".//*[@id='wrapper']/div[2]/div/div[1]/div")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		
		int index = 1;
		try {
			while(driver.findElement(By.id("errormsg_0_GmailAddress")).isDisplayed()){
				
				altEmail = email + index;
				driver.findElement(By.id("GmailAddress")).clear();
				driver.findElement(By.id("GmailAddress")).sendKeys(altEmail);
				driver.findElement(By.xpath(".//*[@id='wrapper']/div[2]/div/div[1]/div")).click();
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				index++;
				
			}
		} catch (Exception e1) {
		
		}
		
		driver.findElement(By.id("Passwd")).sendKeys("!mundo123");
		driver.findElement(By.id("PasswdAgain")).sendKeys("!mundo123");
		
		driver.findElement(By.xpath(".//*[@id='BirthMonth']/div[1]")).click();
		driver.findElement(By.xpath(".//*[@id=':1']")).click();
		driver.findElement(By.id("BirthDay")).sendKeys("09");
		driver.findElement(By.id("BirthYear")).sendKeys("2001");
		
		driver.findElement(By.xpath(".//*[@id='Gender']/div[1]")).click();
		driver.findElement(By.xpath(".//*[@id=':f']/div")).click();
		
/*		driver.findElement(By.xpath(".//*[@id='phone-form-element']/table/tbody/tr/th/div")).click();
		driver.findElement(By.xpath(".//*[@id=':7p']/div/div")).click();
		driver.findElement(By.id("RecoveryPhoneNumber")).sendKeys("6477156102");*/
		
		driver.findElement(By.id("RecoveryEmailAddress")).sendKeys("mundotesting1@gmail.com");
				
		driver.findElement(By.xpath(conXpath)).click();
		
		File file = new File("C:/Users/qauser/Desktop/Output.csv");
		Scanner sc = new Scanner(file);
		
		try {
			while(sc.hasNextLine()){
			
				String[] line = sc.nextLine().split(",");
				if((line[1].trim().toLowerCase()).equals(country.trim().toLowerCase())){
					clickID = line[0];
					break;
				}

			}
			sc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		driver.findElement(By.id(clickID)).click();
		driver.findElement(By.id("submitbutton")).click();
		
		waitForElementByID(30, "iagreebutton");
		
		driver.findElement(By.xpath(".//*[@id='tos-text']/div[4]/p")).click();
		driver.findElement(By.id("iagreebutton")).click();
		
		System.exit(1);
		
	}
	
	public void waitForElementByXpath(int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
		
	public void waitForElementByID(int waitSecond, String ID){
	 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID)));  
	 
	}


  	    
    public void createFrame() throws FileNotFoundException {

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("User Login");
        
        frame.setContentPane(createPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(650,300);
        frame.setSize(500, 300);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

    }  
    
    public JPanel createPanel () throws FileNotFoundException{
    	basePanel = new JPanel();
    	basePanel.setLayout(null);

        title = new JLabel("Email and Country");
        title.setLocation(0,0);
        title.setSize(500, 30);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVisible(true);
        basePanel.add(title);

        leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setLocation(0, 35);
        leftPanel.setSize(245, 160);
        //leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        basePanel.add(leftPanel);

/*        emailLabel = new JLabel("Email:");
        emailLabel.setLocation(0, 0);
        emailLabel.setSize(240, 40);
        emailLabel.setHorizontalAlignment(JLabel.RIGHT);
        leftPanel.add(emailLabel);*/
        
        countryLabel = new JLabel("Enter the target Country:");
        countryLabel.setLocation(0, 50);
        countryLabel.setSize(240, 40);
        countryLabel.setHorizontalAlignment(JLabel.RIGHT);
        leftPanel.add(countryLabel);

        rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setLocation(255, 40);
        rightPanel.setSize(245, 160);
        basePanel.add(rightPanel);

/*        emailTxtBox = new JTextField(100);
        emailTxtBox.setLocation(0, 0);
        emailTxtBox.setSize(200, 30);
        emailTxtBox.setText("@gmail.com");
        rightPanel.add(emailTxtBox);*/
        
        countryCombo = new JComboBox<String>();
        countryCombo.setLocation(0, 50);
        countryCombo.setSize(150, 30);        
		File file = new File("C:/Users/qauser/Desktop/Output.csv");
		Scanner sc = new Scanner(file);
		
		try {
			while(sc.hasNextLine()){
			
				String[] line = sc.nextLine().split(",");
				countryCombo.addItem(line[1]);
				}

			sc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countryCombo.setSelectedItem("United States");
        rightPanel.add(countryCombo);

        createButton = new JButton("Create Account!");
        createButton.setLocation(50, 205);
        createButton.setSize(180, 50);
        createButton.addActionListener(this);
        basePanel.add(createButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setLocation(260, 205);
        cancelButton.setSize(180, 50);
        cancelButton.addActionListener(this);
        basePanel.add(cancelButton);

        //basePanel.setOpaque(true);    
        return basePanel;
    }

    public void actionPerformed(ActionEvent e) {

    	if(e.getSource() == cancelButton){
    		frame.dispose();
    	}
    	
    	if(e.getSource() == createButton){
    		//email = emailTxtBox.getText().replaceAll("@gmail.com", "");
    		country = (String) countryCombo.getSelectedItem();
    		frame.setVisible(false);
    		try {
				start();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    	}

    }
	

}


