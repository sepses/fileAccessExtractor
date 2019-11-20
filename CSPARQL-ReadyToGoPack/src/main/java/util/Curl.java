package util;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Model;




public class Curl {
 
  public static void storeData(String file, String namegraph) throws IOException {
	  //System.out.println(file);
	  String url = "http://localhost:8890/sparql-graph-crud-auth?graph-uri="+namegraph;
  		String user = "dba";
  		String pass = "dba";
	  String command ="curl --digest -u "+user+":"+pass+" -v -X POST -T "+file+" "+url;
	 Runtime.getRuntime().exec(command);
	 
  }
  
  public static void storeInitData(String file, String namegraph) throws IOException {
	  //System.out.println(file);
	  String url = "http://localhost:8890/sparql-graph-crud-auth?graph-uri="+namegraph;
  		String user = "dba";
  		String pass = "dba";
	  String command ="curl --digest -u "+user+":"+pass+" -v -X PUT -T "+file+" "+url;
	 Runtime.getRuntime().exec(command);
	 
  }
  
  public static void produceOutputFile(Model model, String fileName) throws IOException {
      FileWriter out = new FileWriter(fileName);
      try {
      	model.write(out,"N3");    	
      }
      finally {
         model.close();
		  
      }
  }
  
 
}