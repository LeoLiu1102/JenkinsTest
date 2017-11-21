package LearnCode;

import javax.swing.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class newLog3 implements ActionListener{
	
	JFrame frame;
    JPanel basePanel, leftPanel, rightPanel;
    JLabel title, uidLabel, pwdLabel, envLabel, wayLabel;
    JTextField uidTxtBox;
    JPasswordField pwdTxtBox;
    JComboBox<String> envCombo, wayCombo;
    JButton loginButton, cancelButton;
    
    WebDriver driver;
    String userId, password, baseURL, method;
    private List<String> campID = new ArrayList<String>();

/*    public static void main(String[] args) {
    	
        newLog2 obj = new newLog2();
        
        WebDriver driver = new FirefoxDriver();
        obj.setDriver(driver);
    	obj.createFrame();
 
    }*/
    
    newLog3(WebDriver o_driver){
    	setDriver(o_driver);
    	createFrame();
    }
    
    private void createFrame() {

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("User Login");
   
        //frame.setContentPane(createPanel());      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(650,300);
        frame.setSize(500, 300);
        //frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }  
    
    public JPanel createPanel (){

    	basePanel = new JPanel();
    	basePanel.setLayout(null);

        title = new JLabel("Login Screen");
        title.setLocation(0,0);
        title.setSize(500, 30);
        title.setHorizontalAlignment(JLabel.CENTER);
        basePanel.add(title);

        leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setLocation(0, 35);
        leftPanel.setSize(245, 160);
        //leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        basePanel.add(leftPanel);

        uidLabel = new JLabel("Username:");
        uidLabel.setLocation(0, 0);
        uidLabel.setSize(240, 40);
        uidLabel.setHorizontalAlignment(JLabel.RIGHT);
        leftPanel.add(uidLabel);

        pwdLabel = new JLabel("Password:");
        pwdLabel.setLocation(0, 40);
        pwdLabel.setSize(240, 40);
        pwdLabel.setHorizontalAlignment(JLabel.RIGHT);
        leftPanel.add(pwdLabel);
        
        envLabel = new JLabel("Environment:");
        envLabel.setLocation(0, 80);
        envLabel.setSize(240, 40);
        envLabel.setHorizontalAlignment(JLabel.RIGHT);
        leftPanel.add(envLabel);
        
        wayLabel = new JLabel("Way to get campaigns:");
        wayLabel.setLocation(0, 120);
        wayLabel.setSize(240, 40);
        wayLabel.setHorizontalAlignment(JLabel.RIGHT);
        leftPanel.add(wayLabel);

        rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setLocation(255, 40);
        rightPanel.setSize(245, 160);
        basePanel.add(rightPanel);

        uidTxtBox = new JTextField(8);
        uidTxtBox.setLocation(0, 0);
        uidTxtBox.setSize(100, 30);
        uidTxtBox.setText(getLoginInfo().get(0));
        rightPanel.add(uidTxtBox);

        pwdTxtBox = new JPasswordField(8);
        pwdTxtBox.setLocation(0, 40);
        pwdTxtBox.setSize(100, 30);
        pwdTxtBox.setEchoChar('*');
        pwdTxtBox.setText(getLoginInfo().get(1));
        rightPanel.add(pwdTxtBox);
        
        envCombo = new JComboBox<String>();
        envCombo.setLocation(0, 80);
        envCombo.setSize(150, 30);
        envCombo.addItem("Production");
        envCombo.addItem("QA DEV");
        rightPanel.add(envCombo);
        
        wayCombo = new JComboBox<String>();
        wayCombo.setLocation(0, 120);
        wayCombo.setSize(150, 30);
        wayCombo.addItem("Top 10");
        wayCombo.addItem("From CSV File");
        rightPanel.add(wayCombo);

        loginButton = new JButton("Login");
        loginButton.setLocation(130, 205);
        loginButton.setSize(80, 30);
        loginButton.addActionListener(this);
        basePanel.add(loginButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setLocation(280, 205);
        cancelButton.setSize(80, 30);
        cancelButton.addActionListener(this);
        basePanel.add(cancelButton);

        basePanel.setOpaque(true);    
        return basePanel;
    }

    public void actionPerformed(ActionEvent e) {

    	if(e.getSource() == cancelButton){
    		frame.dispose();
    	}
    	
    	if(e.getSource() == loginButton){
    		
    		setUserId(uidTxtBox.getText());
    		setPassword(new String(pwdTxtBox.getPassword()));
    		setBaseUrl( (String) envCombo.getSelectedItem() );
    		setWay((String) wayCombo.getSelectedItem());
    		
    	}

    }
  
	
	public void setDriver(WebDriver o_driver){
		driver = o_driver;
	}
	
	private void setUserId(String uid){
		userId = uid;
	}
	
	public String getUserId(){
		return userId;
	}
	
	private void setPassword(String pwd){
		password = pwd;
	}
	
	public String getPassword(){
		return password;
	}
	
	
	private void setBaseUrl(String env){
		
		if(env.equals("Production")){
			baseURL = "http://www.mm-tracking.com/";
		}
		else{
			baseURL = "http://qa-dev.dev-mt01/";
		}
	}
	
	public String getBaseUrl(){
		return baseURL;
	}
	
	private void setWay(String method){
		
		this.method = method;
		System.out.println("1"+method);
		System.out.println("2"+this.method);
		
		if(method.equals("Top 10")){
			camIdTop10();
		}
		else{
			login();
			CamIdFromFile();
		}
		
	}
	
	public boolean getTop10(){
		System.out.println("3"+method);
		if(method.equals("Top 10")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public List<String> getCampId(){
		return campID;
	}
	
	private List<String> getLoginInfo(){
		
		List<String> credential = new ArrayList<String>();
		 
		
		try{
			
			File file = new File("C:/Users/qauser/Desktop/login.csv");
			Scanner sc = new Scanner(file);
			
			while(sc.hasNextLine()){
			
				credential.add((sc.next()));
		
			}
		
			sc.close();
		}
		catch(Exception e){
			credential.add("");
			credential.add("");
			//System.out.println("File is not found!");
		
		}
		
		return credential;
	}
	
	public void login() {
		
		driver.get(getBaseUrl()+"admin/login");
		//driver.manage().window().maximize();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(getUserId());
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(getPassword());
		driver.findElement(By.id("login")).click();
		String selectLoginXpath = "html/body/div[2]/div/div/div[2]/table/tbody/tr/td[1]/a";
		waitForElementByXpath(30, selectLoginXpath);
		driver.findElement(By.xpath(selectLoginXpath)).click();
		String statsTabXpath = ".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]";
		waitForElementByXpath(30, statsTabXpath);
	  
	}
	
	private void camIdTop10(){
		 
		int numOfClicks;
		int numOfLeads;
		
		login();
		
		String statsTableXpath = ".//*[@id='stats_table']";
		driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li[1]/div/a/div[2]")).click();
		driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li[1]/ul/li[1]/a")).click();
		waitForElementByXpath(30, statsTableXpath);	  
		
		int count = 0;
		int row = 3;
		
		while(count < 10){
		
			//get numbers of clicks and leads, remove the comma(,), then convert it to integer
			numOfClicks = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[12]")).getText().replaceAll(",", ""));
			numOfLeads = Integer.parseInt(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[15]/span")).getText().replaceAll(",", ""));
			
			//get campaign IDs, skill Pay per Call type(leads < click) 
			if(numOfLeads > numOfClicks){			
				row++; 
			}
			else{
				campID.add(driver.findElement(By.xpath(".//*[@id='stats_table']/table/tbody/tr["+row+"]/td[3]")).getText());
				count++;
				row++;
			}
						
		}
	 
	}
	
	private void CamIdFromFile(){
		 
		File file = new File("C:/Users/qauser/Desktop/CampList.csv");
		try{
			
			Scanner sc = new Scanner(file);
			
			while(sc.hasNextLine()){
			
				campID.add((sc.next()));
		
			}
		
			sc.close();
		}
		catch(Exception e){
		
			System.out.println("File is not found!");
		
		}

	}
	
	public void waitForElementByXpath(int waitSecond, String Xpath){
		 
		WebDriverWait wait = new WebDriverWait(driver, waitSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));  
	 
	}
}
		
