package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
	public static void main( String[] args ) throws IOException
    {
	  FileInputStream ip = new FileInputStream("config2.properties");
   	  Properties prop = new Properties();
   	  prop.load(ip);
   	  String cross_machine = prop.getProperty("cross_machine");
   	 
   	  if(cross_machine.equals("no")) {
   		 //System.out.print("simple");System.exit(0);
   		  App5.generateSimpleRandom(prop);
   	  }else {
   		//System.out.print("cross");System.exit(0);
   		  App8Win.generateCrossRandom(prop);
   	  }
    }
}
