import java.awt.Event;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.atlas.json.JsonValue;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
import com.github.jsonldjava.shaded.com.google.common.base.Strings;

public class semanticCorellationRDF {
	
	
	static final String inputFileName  = "input/wineventlogEventCorellation.json";
	public static void main (String args[]) throws IOException {
		processWinEvent(inputFileName);
		hleModel.write(System.out);
		String filename = getFileName(inputFileName);
		 
      String fileName = "output/"+filename+".rdf";
      FileWriter out = new FileWriter(fileName);
      try {
          hleModel.write(out );
      }
      finally {
         try {
      	     out.close();
             
         }
         catch (IOException closeException) {
             // ignore
         }
      }
	}
	
	
	private static void processWinEvent(String inputFile) {  	
    	
        try {
        	
			FileInputStream fis = new FileInputStream(inputFile);
        	BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String query1 =             		
            		"PREFIX winType: <http://purl.org/sepses/vocab/log/wineventlog#>" + 
            		"PREFIX win: <http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#>" +  
            		"SELECT  (str(?objectName) as ?obj) (str(?handleID) as ?hand) (str(?eventID) as ?eve) (str(?processName) as ?proc) (str(?user) as ?account) (str(?accountDomain) as ?host) (str(?accessMask) as ?access) \r\n" + 
            		"WHERE {" + 
            		"  ?x a winType:WindowsEventLogEntry ." + 
            		"  ?x win:accessMask ?accessMask ." + 
            		"  ?x win:objectName ?objectName ." + 
            		"  ?x win:eventID ?eventID ." + 
            		"  ?x win:processName ?processName ." + 
            		"  ?x win:accountDomain ?accountDomain ." + 
            		"  ?x win:hasProtocol ?user ." + 
            		"  ?x win:handleID ?handleID" + 
            		//"  FILTER regex(str(?accessMask), \"0x2\")" + 
            		"}";
            
            Model model = ModelFactory.createDefaultModel();
            Query mQuery = QueryFactory.create(query1);

    		try {
    			
    			
    			
       			while (in.ready()) {
       				String line = in.readLine();
                    model.read(IOUtils.toInputStream(line, "UTF-8"), null, "JSONLD");
                    
                    QueryExecution qexec = QueryExecutionFactory.create(mQuery, model);
        			ResultSet result = qexec.execSelect();
        			//ResultSetFormatter.out(System.out, result);
        			
        		
        			
        			while (result.hasNext()) {
	    				QuerySolution sol = result.nextSolution();
	    				JSONObject jsonLdObject = new JSONObject();
	    				
	    				jsonLdObject.put("objectName",sol.getLiteral("obj").toString());
	    				jsonLdObject.put("processName",sol.getLiteral("proc").toString());
	    				jsonLdObject.put("handleId",sol.getLiteral("hand").toString());
	    				jsonLdObject.put("accountName",sol.getLiteral("account").toString());
	    				jsonLdObject.put("accessMask",sol.getLiteral("access").toString());
	    				jsonLdObject.put("hostName",sol.getLiteral("host").toString());
	    				jsonLdObject.put("eventId",Integer.parseInt(sol.getLiteral("eve").toString()));
	    				
	    				
	    				String objectName = (String) jsonLdObject.get("objectName");
	    				String hostName = (String) jsonLdObject.get("hostName");
	    				String accessMask = (String) jsonLdObject.get("accessMask");
	    				Integer eventId = (Integer) jsonLdObject.get("eventId");
	    				String processName = (String) jsonLdObject.get("processName");
	    				String accountName = (String) jsonLdObject.get("accountName");
	    				String fileName = getFileName(objectName);
	    		        String handleId = (String) jsonLdObject.get("handleId");
	    		        String folderName = getFolderName(objectName);
	    		    	
//	    				
//	    		        System.out.print("\"objectName\":"+objectName+",");
//	    		        System.out.print("\"processName\":"+processName+",");
//	    		        System.out.print("\"handleId\":"+handleId+",");
//	    		        System.out.print("\"accountName\":"+accountName+",");
//	    				System.out.print("\"accessMask\":"+accessMask+",");
//	    				System.out.println("\"eventId\":"+eventId+",");
	    				
	    		      
	    		        switch(eventId)
	    		        {
	    		            case 4656:
	    		                if(!isTemporary(fileName) && pendingRemoves.containsKey(objectName)) {
	    		                    pendingRemoves.get(objectName).put("alive", true);
	    		                }
	    		                break;

	    		            case 4663:
	    		                if(isTemporary(fileName)) break;

	    		                if(isFileName(fileName)){
		    		                if(lastDiffObjectName.containsKey("folderName")){
		    		                	if(folderName.equals((String) lastDiffObjectName.get("folderName"))) {
		    		                		if(!fileName.equals((String) lastDiffObjectName.get("fileName"))) {
		    		                			lastDiffObjectName.replace("fileName",fileName);
		    		                		}
		    		                	}
		    		                	
		    		                	
		    		                }else {
		    		                	lastDiffObjectName.put("folderName",folderName);
		    		                	lastDiffObjectName.put("fileName",fileName);
		    		                }
	    		                	
	    		                    if(handles.containsKey(handleId)){
	    		                        String handleObjectName = (String) handles.get(handleId).get("objectName");
	    		                        if(!handleObjectName.equals(objectName))
	    		                            changes.put(objectName, handleObjectName);
	    		                            //System.out.println("Change: " + handleObjectName + " - " + objectName);
	    		                            
	    		                    } else{
	    		                        //System.out.println("Access: " + objectName);
	    		                        handles.put(handleId,jsonLdObject);
	    		                    }
	    		                }

	    		                if(accessMask.equals("0x10000")){
	    		                    if(pendingRemoves.containsKey(objectName)){
	    		                        pendingRemoves.replace(objectName, jsonLdObject);
	    		                    }
	    		                    else
	    		                        pendingRemoves.put(objectName, jsonLdObject);
	    		                }
	    		                else if(accessMask.startsWith("0x2") && isFileName(fileName)){
	    		                	if(lastDiffObjectName.get("folderName").equals(folderName) ) {
	    		                		System.out.println("created/modified " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId);
	    		                		String logMessage= "created/modified " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId;
	    		                		createResource(objectName,"", processName, accountName, "created/modified", hostName, "", handleId, eventId, accessMask, logMessage);
	    		                		
	    		                	}else if(!fileName.equals(lastDiffObjectName.get("fileName"))) {
	    		                		System.out.println("created/modified " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId);
	    		                		String logMessage= "created/modified " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId;
	    		                		createResource(objectName, "", processName, accountName, "create/modified", hostName, "", handleId, eventId, accessMask, logMessage);
	    		                	}
	    		                	
	    		                	else {
		    		                    System.out.println("create/modified/copied " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId + " from(" + lastDiffObjectName.get("folderName") +"\\"+ lastDiffObjectName.get("fileName") + ")");
		    		                    String targetObjectName = lastDiffObjectName.get("folderName") +"\\" + lastDiffObjectName.get("fileName");
		    		                    String logMessage= "create/modified/copied " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId + " from(" + lastDiffObjectName.get("folderName") +"\\"+ lastDiffObjectName.get("fileName") + ")";
		    		                    createResource( targetObjectName ,objectName, processName, accountName, "create/modified/copied", hostName, "", handleId, eventId, accessMask, logMessage);
	    		                	}
		    		                pendingRemoves.remove(objectName);
	    		                }
	    		                else if(accessMask.startsWith("0x80")){
	    		                    for (String key : pendingRemoves.keySet()) {
	    		                        Map pendingRemove = pendingRemoves.get(key);
	    		                        String pendingRemoveHandleId =  (String) pendingRemove.get("handleID");
	    		                        Boolean confirmed = false;
	    		                        if(pendingRemove.get("confirmed") != null)
	    		                            confirmed = (Boolean)pendingRemove.get("confirmed");

	    		                        if(!key.equals(objectName) && !confirmed && accountName.equals(pendingRemove.get("accountName"))){

	    		                            if(getFileName(objectName).equals(getFileName(key))){
	    		                                System.out.println("User moved " + key + " to " + objectName);
	    		                                String logMessage = "User moved " + key + " to " + objectName;
	    		                                createResource(key,objectName, processName, accountName, "User moved", hostName, "", handleId, eventId, accessMask, logMessage);
	    	    		                    }
	    		                            else if(objectName.contains("$Recycle.Bin")){
	    		                                System.out.println("Moved to recycle bin " + key + " to " + objectName);
	    		                                String logMessage ="Moved to recycle bin " + key + " to " + objectName;
	    		                                createResource(key,objectName, processName, accountName, "moved to recycle bin", hostName, "", handleId, eventId, accessMask, logMessage);
	    	    		                		pendingRemoves.remove(key);
	    		                            }
	    		                            else if(key.contains("$Recycle.Bin")){
	    		                                System.out.println("Moved out of recycle bin " + key + " to " + objectName);
	    		                                String logMessage ="Moved out of recycle bin " + key + " to " + objectName;
	    		                                createResource(key,objectName, processName, accountName, "moved out of recycle bin", hostName, "", handleId, eventId, accessMask, logMessage);
	    	    		                		
	    		                                pendingRemoves.remove(key);
	    		                            }
	    		                            else if(getFolderName(key).equalsIgnoreCase(getFolderName(objectName))){
	    		                                if(getFileName(key).equals("New Folder")) {
	    		                                    System.out.println("Created " + objectName);
	    		                                    String logMessage = "Created " + objectName;
	    		                                    createResource(objectName,"", processName, accountName, "created", hostName, "", handleId, eventId, accessMask, logMessage);
	    		                                }
	    		                                else {
	    		                                     System.out.println("Renamed " + key + " to " + objectName + " handle: " + handleId);
	    		                                     String logMessage = "Renamed " + key + " to " + objectName + " handle: " + handleId;
	 	    		                                 createResource(key,objectName, processName, accountName, "renamed", hostName, "", handleId, eventId, accessMask, logMessage);
	    		                                }
	    		                                pendingRemoves.remove(key);
	    		                            }
	    		                            break;
	    		                        }
	    		                    }
	    		                }

	    		                break;

	    		            case 4659: break;

	    		            case 4660:
	    		                for (String key : pendingRemoves.keySet()) {
	    		                    Map pendingRemove = pendingRemoves.get(key);
	    		                    String pendingRemoveHandleId = (String) pendingRemove.get("handleID");
	    		                    //User equals User

	    		                    if (pendingRemoveHandleId.equals(handleId) && accountName.equals(pendingRemove.get("accountName"))) {
	    		                        pendingRemoves.get(key).put("confirmed", true);
	    		                    }
	    		                }

	    		                break;

	    		            default: break;
	    		        }
        			}
        			
        			model.removeAll();
        			qexec.close();
      			 
        			
        			
        			

    		} 
       			
    		}finally {
    			   try {
                	   in.close();
                     
                       
                   }
                   catch (IOException closeException) {
                       // ignore
                   }
    		}

    		 

        } catch (IOException e) {
            e.printStackTrace();
        }
           
    }
	  private static HashMap<String, Map<String, Object>> pendingRemoves = new HashMap<String, Map<String, Object>>();
	    private static HashMap<String, Map<String, Object>> handles = new HashMap<String, Map<String, Object>>();
	    private static HashMap<String, String> changes = new HashMap<String, String>();
	    private static HashMap<String, String> lastDiffObjectName = new HashMap<String, String>();

	 private static Model hleModel = ModelFactory.createDefaultModel();
	 
	 private static Resource createResource(String sourceObjectName, String targetObjectName, String processName, String accountName, String action, String sourceHost, String targetHost, String handleId, Integer eventId, String accessMask, String logMessage ) {
		 
	     //define rdf property
	        
		 	Property psourceObjectName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#sourceObjectName");
	        Property ptargetObjectName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#targetObjectName");
	        Property pprocessName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#processName"); 
	        Property psourceHost = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#sourceHost"); 
	        Property ptargetHost = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#targetHost");
	        Property paction = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#action");
	        Property puser = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#user");
	        Property phandleId = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#handleId");
	        Property peventId = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#eventId");
	        Property paccessMask = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#accessMask");
	        Property plogMessage = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/log/coreLog#logMessage");
	        
	     
		 Resource res = hleModel.createResource("http://example.org/sepses/hle#"+UUID.randomUUID());
 		
		res.addProperty(RDF.type, "http://sepses.ifs.tuwien.ac.at/vocab/log/winEventLog#WindowsHighLevelEvent");
		res.addProperty(psourceObjectName, sourceObjectName);
 		res.addProperty(pprocessName, processName);
 		res.addProperty(psourceHost, sourceHost);
 		res.addProperty(paction, action);
 		res.addProperty(puser, accountName);
 		res.addProperty(phandleId, handleId);
 		res.addLiteral(peventId, eventId);
 		res.addProperty(paccessMask, accessMask);
 		res.addProperty(plogMessage, logMessage);
 		//if(targetHost!="") {
 			res.addProperty(ptargetObjectName, targetObjectName);
 		//}
 		return res;
		 
	 }   
	 private static String getFileName(String pathString){
        if(!Strings.isNullOrEmpty(pathString)) {
            File file = new File(pathString);
            return file.getName();
        }
        return "";
     }

     private static String getFolderName(String pathString){
         if(!Strings.isNullOrEmpty(pathString)) {
             File file = new File(pathString);
             return file.getParent();
         }
         return "";
     }

     private static boolean isTemporary(String fileName){
         if(fileName == null || fileName.equals(""))
             return false;

         if(fileName.startsWith("~")
             || fileName.equals("thumbs.db")
             || fileName.equals("desktop.ini")
             || fileName.endsWith(".dat"))
             return true;

         return false;
     }

     private static boolean isFileName(String fileName) {
         return fileName.contains(".");
     }
     
     
}
