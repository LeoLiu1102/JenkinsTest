package LearnCode;
import java.util.List;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CheckAllPages {
	
	private WebDriver driver;
	private String username, password, baseUrl;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		int mainsize;
		int[] subsize;
		
		CheckAllPages obj = new CheckAllPages();

		obj.Login();
		
		mainsize = obj.getMainSize();
		
		subsize = new int[mainsize];
		subsize = obj.getSubSize();
		
		obj.clickTabs(mainsize, subsize);
		obj.Quit();

	}
	
	  public void Login() {
	      
		  username = JOptionPane.showInputDialog("Please input your Username: ");  
		  password = JOptionPane.showInputDialog("Please input your Password: ");
		 //baseUrl = JOptionPane.showInputDialog("Please input your test Login URL: ");
		  //baseUrl = "http://qa-dev.dev-mt01/admin/login";
		  baseUrl = "http://www.mm-tracking.com/admin/login";
	      
		  driver = new FirefoxDriver(); 
		  driver.manage().window().maximize();
		  driver.get(baseUrl);
		  driver.findElement(By.id("username")).clear();
		  driver.findElement(By.id("username")).sendKeys(username);
		  driver.findElement(By.id("password")).clear();
		  driver.findElement(By.id("password")).sendKeys(password);
		  driver.findElement(By.id("login")).click();
		  String selectLoginXpath = "html/body/div[2]/div/div/div[2]/table/tbody/tr/td[1]/a";
		  driver.findElement(By.xpath(selectLoginXpath)).click();
	  }
	  
		  
		  
	  
	  
	  public int getMainSize(){
		  
		  WebElement TABbar = driver.findElement(By.id("menu-authentication-bar"));
		  List<WebElement> Tab = TABbar.findElements(By.className("backgroundCenter"));
		  
		  return (Tab.size());


	  }

	  
	  public int[] getSubSize(){

		  int i = 1;
		  int j = 1;
		  
		  WebElement TABbar = driver.findElement(By.id("menu-authentication-bar"));
		  List<WebElement> Tab = TABbar.findElements(By.className("backgroundCenter"));
		  int mainsize = Tab.size();

		  int subsize[] = new int[mainsize];
		  for ( WebElement element : Tab) {
			  
			    WebElement tab = driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+i+"]/ul"));
			    List<WebElement> subTab = tab.findElements(By.tagName("li"));

			    	element.click();
			    	subsize[i-1] = subTab.size();
			    	
			    	i++;
		  }

		  return subsize;
	  }

	  
	  
	  public void clickTabs(int mainsize, int[] subsize) throws InterruptedException{
		  
		  String currentMainTab;
		  String currentSubTab;
		  		  
		  for(int i=1; i<=mainsize; i++){
			  for(int j=0; j<subsize[i-1]; j++){
			  
			  driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+i+"]/div/a/div[2]")).click();
			  currentMainTab = driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+i+"]/div/a/div[2]")).getText();
			  currentSubTab = driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+i+"]/ul/li["+(j+1)+"]/a")).getText();
			  driver.findElement(By.xpath(".//*[@id='menu-authentication-bar']/ul/li["+i+"]/ul/li["+(j+1)+"]/a")).click();


			  String errorTextXpath = "html>body>h1[position()=1]";
			  
			  if(doesElementExist(errorTextXpath)){
			  
				  String text = driver.findElement(By.xpath(errorTextXpath)).getText();
				  
				  if(text.equals("An error occurred"))
					  System.err.println(currentMainTab+" > "+currentSubTab+" Page error occurred.");

			  }
			  else
				  System.out.println(currentMainTab+" > "+currentSubTab+" Page is OK.");
				  
	  
			  }//inner loop end
		  
		  System.out.println("------------------------------------------------");
		  }
		  
	  }
	  
	  
	  public void Quit(){
		  
		  driver.quit();
		  
	  }
	  
	  private boolean doesElementExist (String myObject) throws InterruptedException {
		  try {
			  Thread.sleep(1000);
		      driver.findElement(By.xpath(myObject));
		  } catch (NoSuchElementException e) {
		      return false;
		  }
		  return true;
	  }

}
