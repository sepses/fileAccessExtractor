package SEPSES.fileAccessEvaluator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.JSONArray;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;


import java.util.ArrayList;
import java.util.Scanner;

public class Assesment2 {
 	 private static final ArrayList<String>[] String = null;
	private static Scanner scanner;

	public static void main(String[] args) throws IOException {
			String pathfolder = "D:\\GDriveUndip\\PhD\\FileAccess\\dockerfiles_auditbeat\\agent\\log\\"; 
		 final File folder = new File(pathfolder);
		    //  System.out.print(hostname);System.exit(0);
		 String tripleStoreEP = "http://localhost:8890/sparql";
		 String hostnote= "hostnote.txt";
		      
		 ArrayList<String> listFiles = listFilesForFolder(folder);
		 
	    for (String filename : listFiles) {
			String hostname = filename.substring(4,(filename.lastIndexOf(".")));	
	    //============Cek is File has been read before==========
			
		   boolean hostexist = cekIsHostnameExist(hostname, hostnote);
		    if(hostexist) {
		    	//System.out.println("hostname :"+ hostname+" is already evaluated!");
		    }else {
				//update hostnote
		    	updateHostnote(hostname+"\r", hostnote);
				
				System.out.println("Evaluation on Host: " +hostname);
				//String tot = "";
				
				
		  //==========Actual Event===========================		
				String[] eventType = {"Created", "Modified", "Copied", "Renamed", "Deleted"};
				EventArr ca = cekAsses(pathfolder,filename);
				System.out.print("Actual Event => ");
				int tae = 0;
				for(int i=0;i<eventType.length;i++) {
					System.out.print(eventType[i]+":"+ca.numAc.get(i)+" ");
					tae=tae+ca.numAc.get(i);
				}
				System.out.print("Total:"+tae);
				//tot = cekAllEvent(hostname, tripleStoreEP);
				System.out.println("");
				
		//==========Event in TRIPLESTORE===========================
				System.out.print("Event On TriStore => ");
				int tet = 0;
				int[] numET = totEeachEvent(hostname, tripleStoreEP);
				for(int i=0;i<eventType.length;i++) {
					System.out.print(eventType[i]+":"+numET[i]+" ");
					tet=tet+numET[i];
				}
				System.out.print("Total:"+tet);
				System.out.println("");
				
     //==========Query Result===========================
				
				System.out.print("Query Results => ");
				int trq = 0;
				for(int i=0;i<eventType.length;i++) {
					System.out.print(eventType[i]+":"+ca.numEo.get(i)+" ");
					trq=trq+ca.numEo.get(i);
				}
				System.out.print("Total:"+trq);
				System.out.println("");	
    //========== Relevant Retrieved Event ===========================	
				System.out.println("Relevant Retrieved Event Total: "+ca.relE.size());
				for(int i=0;i<ca.relE.size();i++) {
					System.out.println(ca.relE.get(i));
				}
				
	//========== IRRelevant Event ===========================	
				
				
			String varSource=null;	
				for(int i=0;i<ca.relE.size();i++) {
					if(varSource!=null) {
						varSource=varSource+",<"+ca.relE.get(i)+">";
					}else {
						varSource="<"+ca.relE.get(i)+">";
						
					}
					
			}
				String graphname = "http://w3id.org/sepses/graph/"+hostname;
				
			//System.out.print(varSource);	
				ArrayList<String> IrrE = getIrrelevantSourceEvent(varSource,graphname, tripleStoreEP);
				System.out.println("IRRelevant Event Total: "+IrrE.size());
				for(int i=0;i<IrrE.size();i++) {
					System.out.println(IrrE.get(i));
				}
						
				
				
				//System.out.println("");
			//	System.out.print("Presentation Generation => ");
				
				
//				
//				System.out.println("");
//				float[] pr = getPrecissionRecall(3, 6, 3);
//				System.out.println("Precission:"+pr[0]);
//				System.out.println("Recall:"+pr[1]);
			
		    }
			}
	 }
 	 
 	    private static void updateHostnote(java.lang.String hostname, java.lang.String hostnote) {
		// TODO Auto-generated method stub
 	    	try {
 	    	   
 	    		Files.write(Paths.get(hostnote), hostname.getBytes(), StandardOpenOption.APPEND);
 	          }catch (IOException e) {
 	    	    //exception handling left as an exercise for the reader
 	    	}
		
	}

		private static ArrayList<java.lang.String> getIrrelevantSourceEvent(String varRes, String graphname, String tripleStoreEP) {
 	    	String query3 = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
					"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
					"SELECT ?s FROM <"+graphname+"> WHERE {"
		                    + "?s a fae:FileAccessEvent."
		                    + "?s fae:hasFileAccessType ?fat. "+
		                       "FILTER (str(?s) NOT IN ("+varRes+"))"
							+ "}";
		 //System.out.print(query3);
 	    	
 	    	Query mQuery3 = QueryFactory.create(query3);
		 QueryExecution qexec3 = QueryExecutionFactory.sparqlService(tripleStoreEP,mQuery3);
		 ResultSet result3 = qexec3.execSelect();
	
		 
		 ArrayList<String> irrE = new ArrayList<String>();
	
		 while (result3.hasNext()) {
				QuerySolution sol3 = result3.nextSolution();
				irrE.add(sol3.getResource("s").toString());
		        
		 	}
		 return irrE;
		
	}



		public static int[] totEeachEvent(String hostname, String tripleStoreEP) {
 	    	int[] numET=new int[5];
 	    	String[] eventType = {"Created", "Modified", "Copied", "Renamed", "Deleted"};
 	    	for(int i=0;i<eventType.length;i++) {
 	    		String tot = cekAllEachEvent(hostname, tripleStoreEP, eventType[i]);
 	    		//System.out.print(eventType[i]+":"+tot+" ");
 	    		numET[i]=Integer.parseInt(tot);
 	    	}
 	    	
			
 	    	return numET;
 	    }
 	    
 	    public static float[] getPrecissionRecall(Integer tp, Integer fp , Integer fn ) {
 	    	
 	    	float precission = tp.floatValue()/(tp.floatValue()+fp.floatValue());
 	    	float recall = tp.floatValue()/(tp.floatValue()+fn.floatValue());
 	    	float[] pr = {precission,recall};   	
 	        return pr;
 	     
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
	 
	 public static EventArr cekAsses(String pathfolder, String filename) throws IOException{
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
	        		Integer cr=0;
	        		Integer md=0;
	        		Integer cp=0;
	        		Integer del=0;
	        		Integer ren=0;
	        		
	         		Integer crf=0;
	        		Integer mdf=0;
	        		Integer cpf=0;
	        		Integer delf=0;
	        		Integer renf=0;
	        		
	        		//gather relevant found resource
	        		ArrayList<String> relEvent = new ArrayList<String>();
	        		
	       			while (in.ready()) {
	       				String line = in.readLine();
	       				//System.out.println(line);
	       				JSONArray jsonarray = new JSONArray("["+line+"]");
	       		         String[] cek =  cekExistingEvent(hostname,jsonarray, tripleStoreEP);
	   		            if(Integer.parseInt(cek[1])>1) {
       		            	cek[1]="1";
       		            }
	   		            
	   		         if(cek[2]!=null) {
    		            	relEvent.add(cek[2]);
    		            }
//	   		         else {
//    		            	relEvent.add("lost"+cek[0]);
//    		            }
	       		         	       	     
	       		            if(cek[0].equals("Created")) {
	       		            	cr++;
	       		            	crf=crf+Integer.parseInt(cek[1]);
	       		            	
	       		            }else if(cek[0].equals("Modified")) {
	       		            	md++;
	       		            	mdf=mdf+Integer.parseInt(cek[1]);
	       		            }else if(cek[0].equals("Copied")) {
     		            	   cp++;
     		            	   cpf=cpf+Integer.parseInt(cek[1]);
     		            	
     		            } else if(cek[0].equals("Renamed")) {
       		            	ren++;
       		            	renf=renf+Integer.parseInt(cek[1]);
       		            }else {
       		            	del++;
       		            	delf=delf+Integer.parseInt(cek[1]);
       		            }
	       		            
	       		            
	       		            if(Integer.parseInt(cek[1])>=1) {
	       		            	yes++;
	       		            }else {
	       		            	no++;
	       		            }
	       		         ev++;   
	       				}
//	       			System.out.print("Total Actual Event: "+ev+" => ");
//	       			System.out.print("Created:"+cr);
//	       			System.out.print(" Modified:"+md);
//	       			System.out.print(" Copied:"+cp);
//	       			System.out.print(" Renamed:"+ren);
//	       			System.out.print(" Deleted:"+del);
//	       			System.out.println("");
//	       			System.out.print("Total Event on Result Query => ");
//	       			System.out.print("Created:"+crf);
//	       			System.out.print(" Modified:"+mdf);
//	       			System.out.print(" Copied:"+cpf);
//	       			System.out.print(" Renamed:"+renf);
//	       			System.out.print(" Deleted:"+delf);
//	       			System.out.println("");
//	       			System.out.print("Result => Total Found:"+yes);
//	       			System.out.println(" Total Not Found:"+no);
	       			ArrayList<Integer> numAcEv = new ArrayList<Integer>();
	       			numAcEv.add(cr);
	       			numAcEv.add(md);
	       			numAcEv.add(cp);
	       			numAcEv.add(ren);
	       			numAcEv.add(del);
	       			ArrayList<Integer> numRoQ = new ArrayList<Integer>();
	       			numRoQ.add(crf);
	       			numRoQ.add(mdf);
	       			numRoQ.add(cpf);
	       			numRoQ.add(renf);
	       			numRoQ.add(delf);
	       			
	       		 ArrayList<ArrayList<Integer>> numAcR = new ArrayList<ArrayList<Integer>>();
	       		 EventArr er = new EventArr(numAcEv, numRoQ, relEvent);
	       	    //numAcR.add(numAcEv);
	       	    //numAcR.add(numRoQ);
	       			
	       			return er; 
	       		}finally {
	    			   try {
	                	   in.close();
	                   }
	                   catch (IOException closeException) {
	                       // ignore
	                   }
	    		}
	        }

	private static String[] cekExistingEvent(String hostname,JSONArray jsonarray, String tripleStoreEP) {
		String ev = jsonarray.getJSONObject(0).getString("event");
		String event = ev.substring(0, 1).toUpperCase() + ev.substring(1);
		//System.out.print("Event: "+event+" ");
		
           String sourceFile = jsonarray.getJSONObject(0).getString("sourceFile");
           String sourceHost = jsonarray.getJSONObject(0).getString("host");
           String targetFile = jsonarray.getJSONObject(0).getString("targetFile");
           String date = jsonarray.getJSONObject(0).getString("date");
           String user = jsonarray.getJSONObject(0).getString("user");
           String time =date.substring(0,15);
           //System.out.println(time);
		String graphname = "http://w3id.org/sepses/graph/"+hostname;
		// TODO Auto-generated method stub
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
				"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
				"SELECT (str(count(distinct(?s))) as ?c) FROM <"+graphname+"> WHERE {"
	                    + "?s a fae:FileAccessEvent; "
	                    + "	  fae:hasSourceFile/fae:fileName ?sf  ;"
						+ "	  fae:hasTargetFile/fae:fileName ?tf  ;"
						+ "	  fae:timestamp ?t  ;"
						+ "	  fae:hasSourceHost/fae:hostName \""+sourceHost+"\" ;"		
						+ "	  fae:hasFileAccessType sys:"+event+" ."
						+ "FILTER regex(str(?sf),\""+sourceFile+"\")"
						+ "FILTER regex(str(?tf),\""+targetFile+"\")"
						+ "FILTER regex(str(?t),\""+time+"\")"
						+ "}";
//		if(event.equals("Renamed")) {
//		  System.out.print(query);
//		}
		 Query mQuery1 = QueryFactory.create(query);
		 QueryExecution qexec = QueryExecutionFactory.sparqlService(tripleStoreEP,mQuery1);
		 ResultSet result = qexec.execSelect();
		 String c = null;
		 String s= null;
		 while (result.hasNext()) {
				QuerySolution sol = result.nextSolution();
				c=sol.getLiteral("c").toString();
		
		 	}
		 if(Integer.parseInt(c)<1) {
			 System.out.print(event+":NO");
		 }else {
			
			 String query2 = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
						"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
						"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
						"SELECT ?s FROM <"+graphname+"> WHERE {"
			                    + "?s a fae:FileAccessEvent; "
			                    + "	  fae:hasSourceFile/fae:fileName ?sf  ;"
								+ "	  fae:hasTargetFile/fae:fileName ?tf  ;"
								+ "	  fae:timestamp ?t  ;"
								+ "	  fae:hasSourceHost/fae:hostName \""+sourceHost+"\" ;"		
								+ "	  fae:hasFileAccessType sys:"+event+" ."
								+ "FILTER regex(str(?sf),\""+sourceFile+"\")"
								+ "FILTER regex(str(?tf),\""+targetFile+"\")"
								+ "FILTER regex(str(?t),\""+time+"\")"
								+ "}";
			 Query mQuery2 = QueryFactory.create(query2);
			 QueryExecution qexec2 = QueryExecutionFactory.sparqlService(tripleStoreEP,mQuery2);
			 ResultSet result2 = qexec2.execSelect();
			
			 while (result2.hasNext()) {
					QuerySolution sol2 = result2.nextSolution();
					s=sol2.getResource("s").toString();
			
			 	}
			 
			 
			 System.out.print(event+":Yes");
			 //System.out.print(s);
			 
		 }
		  
		System.out.println(" => findings:"+c+", \"sourceFile\":\""+ sourceFile+"\",\"targetFile\":\""+targetFile+"\",\"date\":\""+time);
		//System.out.println(" => findings:"+c+", \"sourceFile\":\""+ sourceFile+"\",\"targetFile\":\""+targetFile+"\",\"date\":\""+time);
    
		 String[] ret = {event,c,s};
		 
		 return ret;
		
	}
	
	private static String cekAllEvent(String hostname, String tripleStoreEP) {
		String graphname = "http://w3id.org/sepses/graph/"+hostname;
		// TODO Auto-generated method stub
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
				"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
				"SELECT (str(count(distinct(?s))) as ?c) FROM <"+graphname+"> WHERE {"
	                    + "?s a fae:FileAccessEvent. "
	                    + "?s fae:hasFileAccessType ?fat."
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
	
	private static String cekAllEachEvent(String hostname, String tripleStoreEP, String eventType) {
		String graphname = "http://w3id.org/sepses/graph/"+hostname;
		// TODO Auto-generated method stub
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
				"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
				"SELECT (str(count(distinct(?s))) as ?c) FROM <"+graphname+"> WHERE {"
	                    + "?s a fae:FileAccessEvent. "
	                    + "?s fae:hasFileAccessType sys:"+eventType+". "
	 				+ "}";
		//System.out.println(query);
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
	
	static boolean cekIsHostnameExist(String hostname, String filename) throws FileNotFoundException{
		FileInputStream file = new FileInputStream(filename);
		boolean exist=false;
	
		    scanner = new Scanner(file);
		    //now read the file line by line...
		    int lineNum = 0;
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if(line.equals(hostname)) { 
		            exist=true;
		        }
		    }

		return exist;
	}
}

class EventArr{  
     ArrayList<Integer> numAc;
     ArrayList<Integer> numEo;
    ArrayList<String> relE; 
  
    EventArr( ArrayList<Integer> numAc,ArrayList<Integer> numEo,ArrayList<String> relE){  
       this.numAc=numAc;
       this.numEo=numEo;
       this.relE=relE;
    }  
} 
