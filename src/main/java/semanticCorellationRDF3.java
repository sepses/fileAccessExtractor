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

public class semanticCorellationRDF3 {
	
	
	static final String inputFileName  = "input/new/wineventlogwin8.jsonld";
	public static void main (String args[]) throws IOException {
		processWinEvent(inputFileName);
		
		Model result = eventRelationMaterialization(hleModel);
		hleModel.add(result);
		result.write(System.out);
		
		//hleModel.write(System.out);
		
		//System.exit(0);
		String filename = getFileName(inputFileName);
		 
      String fileName = "output/new/"+filename+"10_fd_with_bg.ttl";
      FileWriter out = new FileWriter(fileName);
      try {
          hleModel.write(out,"TURTLE");
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
            		"PREFIX cl: <http://purl.org/sepses/vocab/log/coreLog#>" + 
            		"SELECT  (str(?objectName) as ?obj) (str(?handleID) as ?hand) (str(?eventID) as ?eve) (str(?processName) as ?proc) (str(?user) as ?account) (str(?accountDomain) as ?host) (str(?accessMask) as ?access) (str(?timestamp) as ?tmstm) \r\n" + 
            		"WHERE {" + 
            		"  ?x a winType:WindowsEventLogEntry ." + 
            		"  ?x win:accessMask ?accessMask ." + 
            		"  ?x win:objectName ?objectName ." + 
            		"  ?x win:eventID ?eventID ." + 
            		"  ?x win:processName ?processName ." + 
            		"  ?x win:accountDomain ?accountDomain ." + 
            		"  ?x win:hasProtocol ?user ." + 
            		"  ?x win:handleID ?handleID ." + 
            		"  ?x cl:timestamp ?timestamp" + 
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
	    				
	    				jsonLdObject.put("timestamp",sol.getLiteral("tmstm").toString());
	    				jsonLdObject.put("objectName",sol.getLiteral("obj").toString());
	    				jsonLdObject.put("processName",sol.getLiteral("proc").toString());
	    				jsonLdObject.put("handleId",sol.getLiteral("hand").toString());
	    				jsonLdObject.put("accountName",sol.getLiteral("account").toString());
	    				jsonLdObject.put("accessMask",sol.getLiteral("access").toString());
	    				jsonLdObject.put("hostName",sol.getLiteral("host").toString());
	    				jsonLdObject.put("eventId",Integer.parseInt(sol.getLiteral("eve").toString()));
	    				
	    				
	    				//String timestamp ="2018-09-01T16:35:19.262Z";
	    				String timestamp =(String) jsonLdObject.get("timestamp");
	    				String objectName = (String) jsonLdObject.get("objectName");
	    				String hostName = (String) jsonLdObject.get("hostName");
	    				String accessMask = (String) jsonLdObject.get("accessMask");
	    				Integer eventId = (Integer) jsonLdObject.get("eventId");
	    				String processName = (String) jsonLdObject.get("processName");
	    				String accountName = (String) jsonLdObject.get("accountName");
	    				String fileName = getFileName(objectName);
	    		        String handleId = (String) jsonLdObject.get("handleId");
	    		        String folderName = getFolderName(objectName);

	    				
	    		      
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
	    		                		createResource(timestamp, objectName,
	    		                				"", "",
	    		                				processName,
	    		                				accountName, 
	    		                				"Created/Modified", 
	    		                				hostName, 
	    		                				"",
	    		                				eventId
	    		                				);
	    		                		
	    		       
	    		                		
	    		                	}else if(!fileName.equals(lastDiffObjectName.get("fileName"))) {
	    		                		System.out.println("created/modified " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId);
	    		                		String logMessage= "created/modified " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId;
	    		                		createResource(timestamp,objectName, 
	    		                				"", "",
	    		                				processName, 
	    		                				accountName, 
	    		                				"Create/Modified", 
	    		                				hostName, 
	    		                				"", 	    		                			
	    		                				eventId);
	    		                	}
	    		                	
	    		                	else {
		    		                    System.out.println("create/modified/copied " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId + " from(" + lastDiffObjectName.get("folderName") +"\\"+ lastDiffObjectName.get("fileName") + ")");
		    		                    String targetObjectName = lastDiffObjectName.get("folderName") +"\\" + lastDiffObjectName.get("fileName");
		    		                    String logMessage= "create/modified/copied " + objectName + " process: " + processName + " accountName: " + accountName + " handle: " + handleId + " from(" + lastDiffObjectName.get("folderName") +"\\"+ lastDiffObjectName.get("fileName") + ")";
		    		                    createResource( timestamp,targetObjectName ,objectName, 
		    		                    		getFileName(objectName),
		    		                    		processName, accountName, "Created/Copied", hostName, "", eventId);
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
	    		                                createResource(timestamp, key,objectName,
	    		                                		getFileName(objectName),
	    		                                		processName, accountName, "Moved", hostName, "",eventId);
	    	    		                    }
	    		                            else if(objectName.contains("$Recycle.Bin")){
	    		                                System.out.println("Moved to recycle bin " + key + " to " + objectName);
	    		                                String logMessage ="Moved to recycle bin " + key + " to " + objectName;
	    		                                createResource(timestamp,key,objectName, 
	    		                                		getFileName(objectName),
	    		                                		processName, accountName, "MovedToRecycleBin", hostName, "", eventId);
	    	    		                		pendingRemoves.remove(key);
	    		                            }
	    		                            else if(key.contains("$Recycle.Bin")){
	    		                                System.out.println("Moved out of recycle bin " + key + " to " + objectName);
	    		                                String logMessage ="Moved out of recycle bin " + key + " to " + objectName;
	    		                                createResource(timestamp,key,objectName, 
	    		                                		getFileName(objectName),
	    		                                		processName, accountName, "Deleted", hostName, "", eventId);
	    	    		                		
	    		                                pendingRemoves.remove(key);
	    		                            }
	    		                            else if(getFolderName(key).equalsIgnoreCase(getFolderName(objectName))){
	    		                                if(getFileName(key).equals("New Folder")) {
	    		                                    System.out.println("Created " + objectName);
	    		                                    String logMessage = "Created " + objectName;
	    		                                    createResource(timestamp,objectName,"","", processName, accountName, "Created", hostName, "", eventId);
	    		                                }
	    		                                else {
	    		                                     System.out.println("Renamed " + key + " to " + objectName + " handle: " + handleId);
	    		                                     String logMessage = "Renamed " + key + " to " + objectName + " handle: " + handleId;
	 	    		                                 createResource(timestamp, key,objectName,
	 	    		                                		getFileName(objectName),
	 	    		                                		 processName, accountName, "Renamed", hostName, "", eventId);
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
	 
	 private static Model eventRelationMaterialization(Model model) {
		 String squery ="PREFIX fae: <http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#>\r\n" + 
		 		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
		 		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
		 		"\r\n" + 
		 		"CONSTRUCT {?x fae:relatedTo ?y}   WHERE { {\r\n" + 
		 		"  ?x a fae:FileAccessEvent.\r\n" + 
		 		"  ?y a fae:FileAccessEvent.\r\n" + 
		 		"  ?x fae:hasTargetFile/fae:fileName ?xtfilename.\r\n" + 
		 		"  ?y fae:hasSourceFile/fae:fileName ?ysfilename. \r\n" + 
		 		"  FILTER (?xtfilename = ?ysfilename).\r\n" + 
		 		"  FILTER (?xtfilename != \"\").\r\n" + 
		 		"  FILTER (?x != ?y).\r\n" + 
		 		"}\r\n" + 
		 		"UNION\r\n" + 
		 		" {\r\n" + 
		 		"  ?x a fae:FileAccessEvent.\r\n" + 
		 		"  ?y a fae:FileAccessEvent.\r\n" + 
		 		"  ?x fae:hasSourceFile/fae:fileName ?xsfilename.\r\n" + 
		 		"  ?x fae:hasTargetFile/fae:fileName ?xtfilename.\r\n" + 
		 		"  ?y fae:hasSourceFile/fae:fileName ?ysfilename. \r\n" + 
		 		"  FILTER (?xsfilename = ?ysfilename).\r\n" + 
		 		"  FILTER (?xtfilename = \"\").\r\n" + 
		 		"  FILTER (?x != ?y).\r\n" + 
		 		"}}";
		 Query mQuery1 = QueryFactory.create(squery);
		 QueryExecution qexec = QueryExecutionFactory.create(mQuery1, model);
		 Model result = qexec.execConstruct();
		// ResultSetFormatter.out(System.out, result);
         
		 return result;
		 
	 }
	 private static Resource createResource(
			 String timestamp,
			 String sourceFileName, 
			 String targetFileName,
			 String targetFile,
			 String programName, 
			 String userName, 
			 String actionName, 
			 String sourceHostName,
			 String targetHostName, 
			 Integer eventId ) {
		 
		 
		 
		 
	     //define rdf property
	   	        Property ptimestamp = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#timestamp");
	        		Property phasSourceFile = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasSourceFile");
	        		Property phasTargetFile = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasTargetFile");
	        		Property pfileName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#fileName");
	        		Property pfile = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#targetFile");
	        		Property phasProgram = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasProgram");
	        		Property pprogramName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasProgramName");
	        		Property phasSourceHost = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasSourceHost");
	        		Property phasTargetHost = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasTargetHost");
	        		Property phostName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hostName");
	        		Property pAccessFileType = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasFileAccessType");
	        		//Property phasAction = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasAction");
	        		Property pAccessFileTypeName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#fileAccessTypeName");
	        		Property phasUser = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#hasUser");
	        		Property puserName = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#userName");
	        		Property peventID = hleModel.createProperty("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#eventID");



	     
		 Resource res = hleModel.createResource("http://example.org/sepses/fileAccessEvent#"+UUID.randomUUID());
		 UUID uuidSourceFile = UUID.randomUUID();
		 UUID uuidTargetFile = UUID.randomUUID();
		 UUID uuidSourceHost = UUID.randomUUID();
		 UUID uuidTargetHost = UUID.randomUUID();
		 UUID uuidProgram = UUID.randomUUID();
		 UUID uuidAction = UUID.randomUUID();
		 UUID uuidUser = UUID.randomUUID();
		 String hasSourceFile =  "http://example.org/sepses/#"+uuidSourceFile;
		 String hasTargetFile =  "http://example.org/sepses/#"+uuidTargetFile;
		 String hasProgram =  "http://example.org/sepses/#"+uuidProgram;
		 String accessFileType =  "http://sepses.ifs.tuwien.ac.at/knowledge/sepsesEventKnowledge#"+actionName;
		 //String hasAction =  "http://example.org/sepses/#"+uuidAction;
		 String hasUser =  "http://example.org/sepses/#"+uuidUser;
		 String hasSourceHost =  "http://example.org/sepses/#"+uuidSourceHost;
		 String hasTargetHost =  "http://example.org/sepses/#"+uuidTargetHost;
		 Resource resSourceFile = hleModel.createResource(hasSourceFile);
		 Resource resTargetFile = hleModel.createResource(hasTargetFile);
		 Resource resSourceHost = hleModel.createResource(hasSourceHost);
		 Resource resTargetHost = hleModel.createResource(hasTargetHost);
		 Resource resProgram = hleModel.createResource(hasProgram);
		 Resource resAccessFileType = hleModel.createResource(accessFileType);
		 Resource resUser = hleModel.createResource(hasUser);
		 Resource resType = hleModel.createResource("http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#FileAccessEvent");
		 
		 			//res.addProperty(RDF.type, "http://sepses.ifs.tuwien.ac.at/vocab/event/fileAccessEvent#FileAccessEvent");
 					res.addProperty(RDF.type, resType);
 					res.addProperty(ptimestamp,timestamp);
 					res.addProperty(pfile,targetFile);
 					res.addProperty(phasSourceFile, resSourceFile);
 					res.addProperty(phasTargetFile, resTargetFile); 
 					resSourceFile.addProperty(pfileName, sourceFileName);
 					resTargetFile.addProperty(pfileName, targetFileName);
 					res.addProperty(phasProgram,resProgram);
 					resProgram.addProperty(pprogramName, programName);
 					res.addProperty(phasSourceHost, resSourceHost);
 					res.addProperty(phasTargetHost, resTargetHost); 
 					resSourceHost.addProperty(phostName, sourceHostName);
 					resTargetHost.addProperty(phostName, targetHostName);
 					res.addProperty(pAccessFileType,resAccessFileType);
 					//res.addProperty(phasAction,resAction);
 					resAccessFileType.addProperty(pAccessFileTypeName, actionName);
 					res.addProperty(phasUser,resUser);
 					resUser.addProperty(puserName, userName);
 					res.addLiteral(peventID,eventId);					
 		return null;
		 
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
