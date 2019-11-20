package eu.larkc.csparql.readytogopack;

//import java.util.UUID;

public class QueryLinuxSimple {
	
	/*public static String getUUID() {
		UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
	}*/
	
	/*public static ArrayList<String> generateSubject() {
		ArrayList<String> subject = new ArrayList<String>();
		subject.add("eve:event-"+getUUID());
		subject.add("eve:eventT-"+getUUID());
		return subject;
	}*/
	
	public static final String selectAll = "REGISTER QUERY selectall AS " +
            "SELECT * " +
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE { " +
            "?s ?p ?o . " +	
            "}";
	
	public static final String created = "REGISTER QUERY created AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Created;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:timestamp ?logtimestamp;"
            		+ "         fae:stimestamp ?logtimestamp;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"		
            		+ "         fae:hasUser ?user ."	
            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname ."
            		+ "?user   		 fae:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE { "+
	             		 "?s file:pathName ?filename . " +
			             "?s file:hostName ?hostname . " +
			             "?s file:timestamp ?logtimestamp . " +
			             "?s file:userName ?username ."+
			             "?s file:eventName ?event ."+
			           "FILTER (regex(str(?event),\"created\")) "   
	             + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"event\"),\"-created\")) AS ?subject)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-file\"),\"-created\")) AS ?sourceFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-file\"),\"-created\")) AS ?targetFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-host\"),\"-created\")) AS ?sourceHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-host\"),\"-created\")) AS ?targetHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"user\"),\"-created\")) AS ?user)"+
            "}";
	
	public static final String del = "REGISTER QUERY del AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Deleted;"
                    + "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:timestamp ?logtimestamp;"
            		+ "         fae:stimestamp ?logtimestamp;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"		
            		+ "         fae:hasUser ?user ."	
            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname ."
            		+ "?user   		 fae:userName ?username"
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE {" +
             "?s file:pathName ?filename . " +
             "?s file:hostName ?hostname . " +
             "?s file:eventName ?event ."+
             "?s file:userName ?username ."+
             "?s file:timestamp ?logtimestamp . " +
            "FILTER regex(str(?event),\"deleted\")"+
            "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"event\"),\"-deleted\")) AS ?subject)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-file\"),\"-deleted\")) AS ?sourceFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-file\"),\"-deleted\")) AS ?targetFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-host\"),\"-deleted\")) AS ?sourceHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-host\"),\"-deleted\")) AS ?targetHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"user\"),\"-deleted\")) AS ?user)"
            + "}";
	
	public static final String modif = "REGISTER QUERY modif AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Modified;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:timestamp ?logtimestamp;"
            		+ "         fae:stimestamp ?logtimestamp;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	
            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname ."
            		+ "?user   		 fae:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE { "+
	             		 "?s file:pathName ?filename . " +
			             "?s file:hostName ?hostname . " +
			             "?s file:timestamp ?logtimestamp . " +
			             "?s file:eventName ?event ."+
			             "?s file:userName ?username ."+
			           "FILTER (regex(str(?event),\"updated\")) "
	        + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"event\"),\"-modified\")) AS ?subject)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-file\"),\"-modified\")) AS ?sourceFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-file\"),\"-modified\")) AS ?targetFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-host\"),\"-modified\")) AS ?sourceHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-host\"),\"-modified\")) AS ?targetHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"user\"),\"-modified\")) AS ?user)"+
            "}";
          
	public static final String ren ="REGISTER QUERY ren AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Renamed;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:timestamp ?logtimestamp;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE {"+
		            "?s file:pathName ?filename2 . " +
		            "?s file:hostName ?hostname2 . " +
		            "?s file:timestamp ?logtimestamp . " +
		            "?s file:userName ?username ."+
		            "?s file:eventName ?event2 ."+
			             "{SELECT * WHERE {" +
				             "?r file:pathName ?filename . " +
				             "?r file:hostName ?hostname . " +
				             "?r file:timestamp ?logtimestamp2 . " +
				             "?r file:eventName ?event"+
			            " FILTER regex(str(?event),\"moved\")}}"+
		            " FILTER (regex(str(?event2),\"created\") && ?filename!=?filename2 && ?hostname=?hostname2 )"
		            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"event\"),\"-renamed\")) AS ?subject)"
		            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-file\"),\"-renamed\")) AS ?sourceFile)"
		            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-file\"),\"-renamed\")) AS ?targetFile)"
		            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"source-host\"),\"-renamed\")) AS ?sourceHost)"
		            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"target-host\"),\"-renamed\")) AS ?targetHost)"
		            + "BIND (URI(CONCAT(REPLACE(str(?s),\"LogEntry\",\"user\"),\"-renamed\")) AS ?user)"
            + "}";
            
	
	public static final String copy = "REGISTER QUERY copied AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:Copied;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:timestamp ?logtimestamp;"
            		+ "         fae:stimestamp ?logtimestamp2;" 
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	
            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username"
            		+ "}"+
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE { "+
	             "?s file:pathName ?filename . " +
	             "?s file:hostName ?hostname . " +
	             "?s file:timestamp ?logtimestamp2 . " +
	             "?s file:eventName ?event ."+
	             "{SELECT * WHERE {"+
	             		 "?r file:pathName ?filename2 . " +
			             "?r file:hostName ?hostname2 . " +
			             "?r file:timestamp ?logtimestamp . " +
			             "?r file:eventName ?event2 ."+
			             "?r file:userName ?username ."+
			           "FILTER (regex(str(?event2),\"created\")) "+    
	              "}}"+
	            "FILTER (regex(str(?event),\"IN_OPEN\") && ?filename2!=?filename && ?hostname=?hostname2 && substr(str(?logtimestamp),21) <= substr(str(?logtimestamp2),21) ) "
            + "BIND (URI(CONCAT(REPLACE(str(?r),\"LogEntry\",\"event\"),\"-copied\")) AS ?subject)"
            + "BIND (URI(CONCAT(REPLACE(str(?r),\"LogEntry\",\"source-file\"),\"-copied\")) AS ?sourceFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?r),\"LogEntry\",\"target-file\"),\"-copied\")) AS ?targetFile)"
            + "BIND (URI(CONCAT(REPLACE(str(?r),\"LogEntry\",\"source-host\"),\"-copied\")) AS ?sourceHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?r),\"LogEntry\",\"target-host\"),\"-copied\")) AS ?targetHost)"
            + "BIND (URI(CONCAT(REPLACE(str(?r),\"LogEntry\",\"user\"),\"-copied\")) AS ?user)"+
            "}";
	
	
	
	/*public static final String securedCopied = "REGISTER QUERY secCopy AS " +
			"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX file: <http://w3id.org/sepses/vocab/unix-event#> " +
			"PREFIX fae: <http://w3id.org/sepses/event/file-access#> " +
			"PREFIX sys: <http://w3id.org/sepses/example/system-knowledge#> " +
			"PREFIX eve: <http://w3id.org/sepses/resource/event#> " +
            "CONSTRUCT {"
                    + "?subject fae:hasFileAccessType sys:SecureCopied;"
            		+ "         rdf:type fae:FileAccessEvent;"
            		+ "         fae:logTimestamp ?logtimestamp2;"
            		+ "         fae:hasSourceFile ?sourceFile;"
            		+ "         fae:hasTargetFile ?targetFile;"
            		+ "         fae:hasSourceHost ?sourceHost;"
            		+ "         fae:hasTargetHost ?targetHost;"	
            		+ "         fae:hasUser ?user ."	

            		+ "?sourceFile   fae:fileName ?filename ."
            		+ "?targetFile   fae:fileName ?filename2 ."
            		+ "?sourceHost   fae:hostName ?hostname ."
            		+ "?targetHost   fae:hostName ?hostname2 ."
            		+ "?user   		 fae:userName ?username ."
            		+ "}"+
                 
            "FROM STREAM <ws://localhost:8124/tw/stream> [RANGE 3s STEP 1s] " +
            "WHERE {"
            + " {SELECT * WHERE {"+
            "?s file:hasFile/file:pathname ?filename3 . " +
            "?s file:hasHost/file:hostName ?hostname3 . " +
            "?s file:logtimestamp ?logtimestamp3 . " +
            "?s file:hasEvent/file:eventName ?event3 ." +
            "?s file:hasUser/file:userName ?username"
	            + " {SELECT ?filename2 ?hostname2 ?logtimestamp2 ?event2 ?eventall  ?filename ?hostname  WHERE {"+
	            "?s file:hasFile/file:pathname ?filename2 . " +
	            "?s file:hasHost/file:hostName ?hostname2 . " +
	            "?s file:logtimestamp ?logtimestamp2 . " +
	            "?s file:hasEvent/file:eventName ?event2"+
		             "{SELECT ?filename ?hostname ?logtimestamp ?event WHERE {" +
			            "?s file:hasFile/file:pathname ?filename . " +
			             "?s file:hasHost/file:hostName ?hostname . " +
			             "?s file:logtimestamp ?logtimestamp . " +
			             "?s file:hasEvent/file:eventName ?event"+
		            " FILTER regex(str(?event),\"IN_CREATE\")}"+
	            " }FILTER (regex(str(?event2),\"IN_OPEN\") && ?filename2=?filename && ?logtimestamp2=?logtimestamp && ?hostname2=?hostname ) BIND (CONCAT(str(?event),\"_\",str(?event2)) AS ?eventall)}"
	         + "} FILTER (regex(str(?event3),\"IN_MODIFY\") && ?filename3=?filename2 && ?logtimestamp3=?logtimestamp2 && ?hostname3=?hostname2 )"
	            + "BIND (CONCAT(str(?eventall),\"_\",str(?event3)) AS ?eventalll)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"event\")) AS ?subject)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-file\")) AS ?sourceFile)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-file\")) AS ?targetFile)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"source-host\")) AS ?sourceHost)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"target-host\")) AS ?targetHost)"
	            + "BIND (URI(REPLACE(str(?s),\"LogEntry\",\"user\")) AS ?user)"
	            + "}} "+
             "FILTER regex(str(?eventalll),\"IN_CREATE_IN_OPEN_IN_MODIFY\")}";*/
	
}
