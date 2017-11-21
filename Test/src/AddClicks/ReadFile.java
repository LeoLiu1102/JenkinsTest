package AddClicks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {
	
	//List<String> campInfo;
	
	ReadFile(String filePath, List<String> campInfo){
		File file = new File(filePath);
		read(file, campInfo);
	}
	
	public void read(File file, List<String> campInfo){
		
		//List<String> campInfo = new ArrayList<String>(); 

		try{
			
			Scanner sc = new Scanner(file);
			
			while(sc.hasNextLine()){
			
				campInfo.add((sc.nextLine()));
		
			}
		
			sc.close();
		}
		catch(Exception e){
		
			System.out.println("File is not found!");
		
		}


	}

}
