package LearnCode;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class newLog2{
   
	public static void main(String[] args) {
		
		String country = "Germany";
		String[] locales = Locale.getISOCountries();
		String finalCode = "";
		
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
		}
		
	}
    
  
}
		
