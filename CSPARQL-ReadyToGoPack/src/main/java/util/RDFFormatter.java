package util;

import java.io.IOException;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.ResultFormatter;

public class RDFFormatter extends ResultFormatter {
   private String action;
   private String fileName;
   private String tripleStoreEp;
	
	public RDFFormatter(String action){
		this.action=action;
		this.fileName =  "output/"+this.action+".n3";
		this.tripleStoreEp = "http://localhost:8890/sparql";
	}
	@Override
	public void update(Observable o, Object arg) {
		RDFTable q = (RDFTable) arg;
		
		//System.out.println();
		System.out.println("-------"+ q.size() + " results at SystemTime=["+System.currentTimeMillis()+"]--------"); 
		
		
		Model model = ModelFactory.createDefaultModel();
		
		for (final RDFTuple t : q) {
			
			Resource res = model.createResource(t.get(0).toString());
			Property prop = model.createProperty(t.get(1).toString());
			
			String obj = t.get(2).toString();
            String URIRegex = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
            Pattern p = Pattern.compile(URIRegex);
            Matcher m = p.matcher(obj);//replace
			if (m.find()) {
				Resource rObj = model.createResource(obj);
				model.add(res, prop, rObj);
				
			} else {
				model.add(res, prop, obj);
				
			}
			
			
		}
		
		
		
		//======get property========
		String hostname = getHostName(model);
		String hostnameProcess = getHostNameFromProcess(model);
		//String copyEvent = getCopyEvent(model);
		//String createEvent = getCreateEvent(model);
		
		//System.out.println("resourceID: "+resourceId);
		
		//======cek and remove false Created======
		/*
		if (copyEvent!=null) {
			//remove false created
			System.out.println("copyEvent ada: "+copyEvent);
			String graphname = "http://w3id.org/sepses/graph/"+hostname;
			System.out.println("didelete");
			
			
			
			try {
				removeCreatedTripleLevel2(this.tripleStoreEp,copyEvent,graphname);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
			
			try {
				removeCreatedTriple(this.tripleStoreEp,copyEvent,graphname);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
			
			
		}
		
		if (createEvent!=null) {
			//remove false created
			System.out.println("createEvent ada: "+createEvent);
			
			//check existing resource 
			String existingResource = checkExistingCreateResource(this.tripleStoreEp,createEvent);
			
			if(existingResource!=null) {
				System.out.println("existing resource ada:"+ existingResource);
				model.removeAll();
			}
			
			
			
		}*/
		
		//model.write(System.out,"N3");
		
		
        //======outputfile=========
		
		try {
			Curl.produceOutputFile(model,this.fileName);
			System.out.println("produce Output file");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		//======storefileto triplestore      
	    try {
	    	if(hostname!=null) {
			  Curl.storeData(this.fileName, "http://w3id.org/sepses/graph/"+hostname);
			  System.out.println("Store Data");
	    	}else if(hostnameProcess!=null) {
				  Curl.storeData(this.fileName, "http://w3id.org/sepses/graph/"+hostnameProcess);
				  System.out.println("Store Data");
			//model.removeAll();
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	           
	}
	
		private String checkExistingCreateResource(String tripleStoreEp2, String createEvent) {
			String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
					"SELECT ?s WHERE {"
		                    + "?s a fae:FileAccessEvent . "
		            		+ "FILTER (?s=<"+createEvent+">)}";
			//System.out.print(query);System.exit(0);
			 Query mQuery1 = QueryFactory.create(query);
			 QueryExecution qexec = QueryExecutionFactory.sparqlService(tripleStoreEp2,mQuery1);
			 ResultSet result = qexec.execSelect();
			 String host = null;
			 while (result.hasNext()) {
					QuerySolution sol = result.nextSolution();
					host=sol.getLiteral("?s").toString();
			// TODO Auto-generated method stub
			
		}
			 return host;
		
	}
		private void removeCreatedTriple(String tripleStoreEp, String copyEvent, String graphname) {
			String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
					"WITH <"+graphname+"> DELETE {?s ?p ?o} WHERE {"
		                    + "?s ?p ?o . "
		                    + "FILTER (?s=<"+copyEvent+">)}";
			System.out.println("delete mulai...");
			System.out.println(query);
			System.out.println(tripleStoreEp);
			UpdateRequest Q1 = UpdateFactory.create(query);
			UpdateProcessor qeQ1 = UpdateExecutionFactory.createRemote(Q1, tripleStoreEp);
			qeQ1.execute();
			System.out.println("delete selesai...");
			 
	}
		
	private void removeCreatedTripleLevel2(String tripleStoreEp, String copyEvent, String graphname) {
		
			String query2 = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
					"WITH <"+graphname+"> DELETE {?o ?p1 ?o1} WHERE {"
		                    + "?s ?p ?o ."
		                    + "?o ?p1 ?o1. "
		                    + "FILTER (?s=<"+copyEvent+">)}";
		    
		    
			System.out.println("delete dalam mulai...");
			System.out.println(query2);
			System.out.println(tripleStoreEp);
			UpdateRequest Q2 = UpdateFactory.create(query2);
			UpdateProcessor qeQ2 = UpdateExecutionFactory.createRemote(Q2, tripleStoreEp);
			qeQ2.execute();
			System.out.println("delete dalam selesai...");
			
	}
		private String getHostName(Model model) {
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
				"SELECT ?host WHERE {"
	                    + "?s a fae:FileAccessEvent;"
	            		+ "     fae:hasSourceHost/fae:hostName ?host ."
	            		+ "}";
		
		 Query mQuery1 = QueryFactory.create(query);
		 QueryExecution qexec = QueryExecutionFactory.create(mQuery1, model);
		 ResultSet result = qexec.execSelect();
		 String host = null;
		 while (result.hasNext()) {
				QuerySolution sol = result.nextSolution();
				host=sol.getLiteral("host").toString();
		// TODO Auto-generated method stub
		
	}
		 return host;
	}
	
		private String getHostNameFromProcess(Model model) {
			String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
					"PREFIX se: <http://w3id.org/sepses/event/systemEvent#> " +
					"SELECT ?host WHERE {"
		                    + "?s a se:SystemEvent;"
		            		+ "     se:hasHost/se:hostName ?host ."
		            		+ "}";
			
			 Query mQuery1 = QueryFactory.create(query);
			 QueryExecution qexec = QueryExecutionFactory.create(mQuery1, model);
			 ResultSet result = qexec.execSelect();
			 String host = null;
			 while (result.hasNext()) {
					QuerySolution sol = result.nextSolution();
					host=sol.getLiteral("host").toString();
			// TODO Auto-generated method stub
			
		}
			 return host;
		}
	private String getCopyEvent(Model model) {
		// TODO Auto-generated method stub
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
				"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
				"SELECT ?s WHERE {"
	                    + "?s a fae:FileAccessEvent;"
	            		+ "     fae:hasFileAccessType sys:Copied ."
	            		+ "}";
		//System.out.print(query);
		 Query mQuery1 = QueryFactory.create(query);
		 QueryExecution qexec = QueryExecutionFactory.create(mQuery1, model);
		 ResultSet result = qexec.execSelect();
		 String fat = null;
		 while (result.hasNext()) {
				QuerySolution sol = result.nextSolution();
				fat=sol.getResource("s").toString();
				//System.out.print(fat);
				
		
	}
		 return fat;
	}
	
	private String getCreateEvent(Model model) {
		// TODO Auto-generated method stub
		String query = " PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
				"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
				"SELECT ?s WHERE {"
	                    + "?s a fae:FileAccessEvent;"
	            		+ "     fae:hasFileAccessType sys:Created ."
	            		+ "}";
		//System.out.print(query);
		 Query mQuery1 = QueryFactory.create(query);
		 QueryExecution qexec = QueryExecutionFactory.create(mQuery1, model);
		 ResultSet result = qexec.execSelect();
		 String fat = null;
		 while (result.hasNext()) {
				QuerySolution sol = result.nextSolution();
				fat=sol.getResource("s").toString();
				//System.out.print(fat);
				
		
	}
		 return fat;
	}
}