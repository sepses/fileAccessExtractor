package SEPSES.fileAccessEvaluator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONArray;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;


import java.util.ArrayList;

public class Assesment {
 	 public static void main(String[] args) throws IOException {
			String pathfolder = "D:\\GDriveUndip\\PhD\\FileAccess\\dockerfiles\\agent\\log\\"; 
		 final File folder = new File(pathfolder);
		  ArrayList<String> listFiles = listFilesForFolder(folder);
		 
			for (String filename : listFiles) {
				//System.out.println(filename);
				cekAsses(pathfolder,filename);
			}
	 }
	 
		public static ArrayList<String> listFilesForFolder(final File folder) {
			ArrayList<String> listFiles = new ArrayList<String>();
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            listFilesForFolder(fileEntry);
		        } else {
		        	listFiles.add(fileEntry.getName());
		        }
		    }
		   return listFiles; 
		}
	 
	 public static void cekAsses(String pathfolder, String filename) throws IOException{
		      String inputFileName  = pathfolder+filename;
		      String hostname = filename.substring(4,(filename.lastIndexOf(".")));
		    //  System.out.print(hostname);System.exit(0);
		      String tripleStoreEP = "http://localhost:8890/sparql";
		      
	     		FileInputStream fis = new FileInputStream(inputFileName);
	        	BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		 
	        	try {
	        		Integer ev =0;
	        		Integer yes = 0;
	        		Integer no = 0;
	       			while (in.ready()) {
	       				String line = in.readLine();
	       				//System.out.println(line);
	       				JSONArray jsonarray = new JSONArray("["+line+"]");
	       		         String cek =  cekExistingEvent(hostname,jsonarray, tripleStoreEP);
	       		            System.out.println(cek);
	       		            if(Integer.parseInt(cek)>=1) {
	       		            	yes++;
	       		            }else {
	       		            	no++;
	       		            }
	       		         ev++;   
	       				}
	       			System.out.print("Total Event: "+ev);
	       			System.out.print(" Total Event found: "+yes);
	       			System.out.print(" Total Event not found: "+no);
	       			
	       		}finally {
	    			   try {
	                	   in.close();
	                   }
	                   catch (IOException closeException) {
	                       // ignore
	                   }
	    		}
	        }

	private static String cekExistingEvent(String hostname,JSONArray jsonarray, String tripleStoreEP) {
		String ev = jsonarray.getJSONObject(0).getString("event");
		String event = ev.substring(0, 1).toUpperCase() + ev.substring(1);
		
           String sourceFile = jsonarray.getJSONObject(0).getString("sourceFile");
           String sourceHost = jsonarray.getJSONObject(0).getString("host");
           String targetFile = jsonarray.getJSONObject(0).getString("targetFile");
           String date = jsonarray.getJSONObject(0).getString("date");
           String user = jsonarray.getJSONObject(0).getString("user");
           String time =date.substring(0,20);
		String graphname = "http://w3id.org/sepses/graph/"+hostname;
		// TODO Auto-generated method stub
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/fileAccessEvent#> " +
				"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
				"SELECT (str(count(?s)) as ?c) FROM <"+graphname+"> WHERE {"
	                    + "?s a fae:FileAccessEvent; "
	                    + "   fae:logTimestamp ?l ; "
	                    + "	  fae:hasSourceFile/fae:fileName ?sf  ;"
						+ "	  fae:hasTargetFile/fae:fileName ?tf  ;"
						+ "	  fae:logTimestamp ?t  ;"
						+ "	  fae:hasSourceHost/fae:hostName \""+sourceHost+"\" ;"
						+ "	  fae:hasUser/fae:userName \""+user+"\" ;"		
						+ "	  fae:hasFileAccessType sys:"+event+" ."
						+ "FILTER regex(str(?sf),\""+sourceFile+"\")"
						+ "FILTER regex(str(?tf),\""+targetFile+"\")"
						+ "FILTER regex (str(?t),\""+time+"\")"
						+ "}";
		//System.out.print(query);System.exit(0);
		 Query mQuery1 = QueryFactory.create(query);
		 QueryExecution qexec = QueryExecutionFactory.sparqlService(tripleStoreEP,mQuery1);
		 ResultSet result = qexec.execSelect();
		 String c = null;
		 while (result.hasNext()) {
				QuerySolution sol = result.nextSolution();
				c=sol.getLiteral("c").toString();
		
		 	}
		 return c;
		
	}
	
}


