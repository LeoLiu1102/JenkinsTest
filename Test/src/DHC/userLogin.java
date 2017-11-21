package DHC;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class userLogin implements ActionListener{
	
	JFrame frame;
    JPanel basePanel, leftPanel, rightPanel;
    JLabel title, uidLabel, pwdLabel, envLabel, wayLabel;
    JTextField uidTxtBox;
    JPasswordField pwdTxtBox;
    JComboBox<String> envCombo, wayCombo;
    JButton loginButton, cancelButton;
    String filePath;
    
    static String userId, password, baseURL, method;
    
    userLogin(String filePath){
    	this.filePath = filePath;
    }
    
    public void createFrame() {

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("User Login");
        
        frame.setContentPane(createPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(650,300);
        frame.setSize(500, 300);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

    }  
    
    public JPanel createPanel (){
    	basePanel = new JPanel();
    	basePanel.setLayout(null);

        title = new JLabel("Login Screen");
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
    		setCollectMethod((String) wayCombo.getSelectedItem());
    		
    		DailyHealthCheck obj = new DailyHealthCheck();
    		frame.setVisible(false);
    		frame.dispose();
    		obj.startTest();
    		
    	}

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
	
	private void setCollectMethod(String m){		
		method = m;
	}
	
	public String getMethod(){
		return method;
	}
	
	public boolean getTop10(){
		if(method.equals("Top 10")){
			return true;
		}
		else{
			return false;
		}
	}
	
	private List<String> getLoginInfo(){
		
		List<String> credential = new ArrayList<String>();
		 
		
		try{
			
			File file = new File(filePath+"login.csv");
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

}
		
