package AddClicksAndLeads;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ReadCSV {

	File file;
	
	ReadCSV(String filePath, List<String> csvFeed){
		file = new File(filePath);
		this.read(file, csvFeed);
		this.validateCSV(csvFeed);
		
	}
	
	public void read(File file, List<String> csvFeed){

		try{			
			Scanner sc = new Scanner(file);			
			while(sc.hasNextLine()){			
				csvFeed.add((sc.nextLine()));		
			}		
			sc.close();
		}
		catch(Exception e){		
			System.out.println("File is not found!");		
		}


	}
	
	
	public void validateCSV(List<String> csvFeed){
		
		String csvError;
		int errorCount;
		int click;
		int uClick;
		int lead;
		String leadHash;
		
		do{
			csvError = "";
			errorCount = 0;

			for(int i = 1; i <= csvFeed.size(); i++){
				//abstract click, unique click and lead numbers
				String[] temp = csvFeed.get(i-1).split(",");
				click = Integer.parseInt(temp[1].trim());
				uClick = Integer.parseInt(temp[2].trim());
				try{
					lead = Integer.parseInt(temp[3].trim());
				}catch(Exception e){
					lead = 0;
				}
				try{
					leadHash = temp[4].trim();
				}catch(Exception e){
					leadHash = "";
				}

				
				if(uClick > click){
					csvError = csvError + "Line " + i + ": Unique Click number can not be greater than Click number!\n";
					errorCount++;
				}
				
				if(click == 0 || uClick == 0){
					csvError = csvError + "Line " + i + ": Unique Click number or Click number can not be 0!\n";
					errorCount++;
				}
				
				if(lead > uClick){
					csvError = csvError + "Line " + i + ": Lead number can not be greater than Unique Click number!\n";
					errorCount++;
				}
				
				if(lead > 0 && leadHash.isEmpty()){
					csvError = csvError + "Line " + i + ": Lead Hash code must be provided if lead number is greater than 0!\n";
					errorCount++;
				}
			}
			
			if(errorCount > 0){
				String message = "<html><body><div><font color=red size=5>Please correct the following errors in csv file, "
						+ "then click OK</font></div></body></html>\n" + csvError;
				this.showMessage(message);
				
				//read file again
				csvFeed.clear();
				this.read(file, csvFeed);
			}
		}while(errorCount > 0);
		
	}
	
	public void showMessage(String message){
		JOptionPane.showMessageDialog(null, message);
		
	}


}
